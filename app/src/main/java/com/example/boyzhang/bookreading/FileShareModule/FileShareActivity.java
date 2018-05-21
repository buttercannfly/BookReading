package com.example.boyzhang.bookreading.FileShareModule;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.example.boyzhang.bookreading.R;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileShareActivity
        extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener ,WifiP2pOperator.Notifier{

    private static final String TAG = "FileShareModule";

    public static final String PROGRESS_SHOW="android.intent.action.PROGRESS_SHOW";
    public static final String PROGRESS_UPDATE="android.intent.action.PROGRESS_UPDATE";
    public static final String PROGRESS_CLOSE="android.intent.action.PROGRESS_CLOSE";

    public static final String IS_EXTRA_DATA="is_extra_data";
    public static final String FILE_LIST="file_list";

    //动态申请sd卡写权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private final int REQUEST_CODE_FOR_FILE_CHOOSE = 1000;

    //发送文件方为Client，接受文件方位Server
    private boolean isClient=false;

    //用于显示文件
    private List<String> filePaths = new ArrayList<String>();
    private Set<String> filePathsSet=new HashSet<String>();

    private ListView fileListView = null;

    //用于显示wifip2p搜索到的设备
    private ListView deviceListView=null;
    private PopupWindow popupWindow=null;
    private TextView textNoDeviceTip=null;

    //wifip2p
    private WifiP2pOperator mWifiP2pOperator=null;
    private List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();

    private ProgressDialog progressDialog;
    private TransferBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_share);

        initData();

        //增加或删除要发送的文件
        Button btnAddFile = (Button) findViewById(R.id.btnAddFile);
        Button btnSend=(Button)findViewById(R.id.btnSend);
        btnAddFile.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        //通过ListView来显示从文件选择器中选择的文件
        initFileListView();

        initBroadcastReceiver();

        initWifiP2p();

        //通过popupWindow 及ListView来显示wifip2p搜索到的设备
        initPopupWindow();

        initProgressDialog();

        //动态申请sd卡写权限
        dynamicPermissionRequest();


    }

    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle==null) return;
        Boolean in=bundle.getBoolean(IS_EXTRA_DATA,false);
        if(in){
            List<String> s=bundle.getStringArrayList(FILE_LIST);
            if(s!=null)
                filePaths.addAll(s);
        }
    }

    private void initFileListView(){
        fileListView = (ListView) findViewById(R.id.listFileList);
        fileListView.setAdapter(new ListFileAdapter(this, R.layout.listview_file_select, filePaths));
    }

    private void initBroadcastReceiver(){
        IntentFilter i=new IntentFilter();
        i.addAction(PROGRESS_CLOSE);
        i.addAction(PROGRESS_SHOW);
        i.addAction(PROGRESS_UPDATE);

        mBroadcastReceiver=new TransferBroadcastReceiver();
        registerReceiver(mBroadcastReceiver,i);
    }

    private void initWifiP2p(){
        mWifiP2pOperator=new WifiP2pOperator(this,this);
        mWifiP2pOperator.open();
    }


    private void initPopupWindow(){
        View view= LayoutInflater.from(this).inflate(R.layout.popupwindow_layout,null);
        deviceListView=(ListView)view.findViewById(R.id.listDevices);
        deviceListView.setAdapter(new ListDeviceAdapter(this,R.layout.listview_devices_item,peers));
        deviceListView.setOnItemClickListener(this);

        Button btnRefreshDevice=(Button)view.findViewById(R.id.btnRefreshDevice);
        btnRefreshDevice.setOnClickListener(this);

        textNoDeviceTip=(TextView) view.findViewById(R.id.textNoDeviceTip);

        popupWindow=new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
    }

    private void initProgressDialog(){
            if(mBroadcastReceiver != null){
                mBroadcastReceiver.setProgressHandle(new ProgressDialogHandle() {
                    @Override
                    public void showProgressDialog(Intent intent) {
                        popupWindow.dismiss();

                        Log.i(TAG, "showProgressDialog: ");
                        String title=null,
                                message=null;
                        if(isClient){
                            title="Sending";
                            message="Prepare Sending Files";
                        }else{
                            title="Receiving";
                            message="Prepare Recviving Files";
                        }
                        
                        if(progressDialog!=null) progressDialog.dismiss();
                       progressDialog=new ProgressDialog(FileShareActivity.this);
                       progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                       progressDialog.setCancelable(false);
                       progressDialog.setCanceledOnTouchOutside(false);
                       progressDialog.setTitle(title);
                       progressDialog.setMessage(message);
                       progressDialog.show();
                    }

                    @Override
                    public void updateProgressDialog(Intent intent) {



                        int count = intent.getIntExtra("COUNT",0);
                        String filename= intent.getStringExtra("FILENAME");
                        int progress=intent.getIntExtra("PROGRESS",0);


                        Log.i(TAG, "updateProgressDialog: ["+progress+"]");

                        if(intent.getBooleanExtra("NEWFILE",false)) {
                            progressDialog.dismiss();

                            progressDialog=new ProgressDialog(FileShareActivity.this);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setTitle("File Transfer");
                            progressDialog.setMessage("[" + count + "]:" + filename);
                            progressDialog.setMax(100);
                            progressDialog.setProgress(intent.getIntExtra("PROGRESS", 0));
                            progressDialog.show();
                        }

                        progressDialog.incrementProgressBy(progress-progressDialog.getProgress());
                    }

                    @Override
                    public void closeProgressDialog(Intent intent) {
                        Log.i(TAG, "closeProgressDialog: ");

                        //mWifiP2pOperator.disconnect();

                        progressDialog.dismiss();

                        progressDialog=new ProgressDialog(FileShareActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("Work Done");
                        progressDialog.setMessage("transfer "+intent.getIntExtra("COUNT",0) + " file");
                        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.dismiss();

                                restartActivity();
                            }
                        });

                        progressDialog.setMax(100);
                        progressDialog.incrementProgressBy(100);
                        progressDialog.show();
                    }
                });
            }
    }

    private void restartActivity(){
        mWifiP2pOperator.close();

        Log.i(TAG, "restartActivity: ");
        Intent intent=new Intent(FileShareActivity.this,FileShareActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean(IS_EXTRA_DATA,true);
        bundle.putStringArrayList(FILE_LIST,new ArrayList<String>(filePaths));
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        overridePendingTransition(0,0);
        FileShareActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mWifiP2pOperator.isOpen()){
            mWifiP2pOperator.close();
        }

        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddFile: {
                Intent i = new Intent(this, FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, REQUEST_CODE_FOR_FILE_CHOOSE);
            }
            break;
            case R.id.btnSend:{
                if(filePaths==null || filePaths.isEmpty()){
                    Toast.makeText(this,"Please choose File",Toast.LENGTH_SHORT).show();
                    return;
                }
                //当决定发送文件时，则用popupWindow显示设备信息
                popupWindow.showAtLocation(view, Gravity.LEFT|Gravity.BOTTOM,0,0);
                //若未搜索到附件设备则提示无设备
                checkPeersEmpty();
            }
            break;
            //这个按钮在popupWindow中
            case R.id.btnRefreshDevice:{
                //可在popupwindow中选择刷新设备，及重新搜索附件设备，并将其显示出来
                checkPeersEmpty();
                //刷新popupwindow中的ListView的数据源
                deviceListView.setAdapter(new ListDeviceAdapter(this,R.layout.listview_devices_item,peers));
                popupWindow.update();
            }
            break;
            default:
                break;
        }
    }

    private boolean checkPeersEmpty(){
        boolean re=false;
        if(peers.isEmpty()){
            textNoDeviceTip.setVisibility(View.VISIBLE);
            re=false;
        }else {
            textNoDeviceTip.setVisibility(View.INVISIBLE);
            re=true;

        }

        return re;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_FOR_FILE_CHOOSE: {
                List<String> list = new ArrayList<String>();
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri: files) {
                    list.add(uri.getPath());
                    File file = Utils.getFileForUri(uri);
                    // Do something with the result...
                }

                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();

                //使用set来保证当文件选择器返回重复的文件时，文件路径不会被重复保存
                filePathsSet.clear();
                filePathsSet.addAll(filePaths);
                filePathsSet.addAll(list);

                filePaths.clear();
                filePaths.addAll(filePathsSet);

                //更新文件ListView的数据
                ((ListFileAdapter) (fileListView.getAdapter())).notifyDataSetChanged();
            }
            break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(adapterView==deviceListView){
            Log.i(TAG, "onItemClick: deviceListView");

            isClient=true;

            WifiP2pDevice device=new WifiP2pDevice(peers.get(i));

            mWifiP2pOperator.connectWith(device);
        }
    }

    private void dynamicPermissionRequest(){
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyConnected() {
        Log.i(TAG, "notifyConnected: ");
        Intent intent=new Intent(this,FileTransferService.class);
        intent.putStringArrayListExtra(FileTransferService.FILE_ARRAY,(ArrayList<String>)filePaths);
        intent.putExtra(FileTransferService.IS_Client,isClient);
        intent.putExtra(FileTransferService.IS_GROUP_OWNER,mWifiP2pOperator.isGroupOwner());
        intent.putExtra(FileTransferService.GROUP_OWNER_ADDRESS,mWifiP2pOperator.getGroupOwnerIp());

        startService(intent);
    }

    @Override
    public void notifyPeersChanged() {
        Log.i(TAG, "notifyPeersChanged: ");
        peers.clear();
        peers.addAll(mWifiP2pOperator.getDeviceList());

        checkPeersEmpty();
        //刷新popupwindow中的ListView的数据源
        deviceListView.setAdapter(new ListDeviceAdapter(this,R.layout.listview_devices_item,peers));
        popupWindow.update();
    }
}
