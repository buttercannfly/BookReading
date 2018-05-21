package com.example.boyzhang.bookreading.FileShareModule;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FileTransferService extends IntentService {

    private static final String TAG = "FileTransferService";

    public static final String FILE_ARRAY = "file_array";
    public static final String IS_Client = "isClient";
    public static final String IS_GROUP_OWNER = "isGroupOwner";
    public static final String GROUP_OWNER_ADDRESS = "groupOwnerAddress";

    public static final int IP_TRANSFER_PORT = 9999;
    public static final int FILE_TRANSFER_PORT = 9998;

    public static final String FILE_TRANSFER_END = "file_transfer_end";

    private List<String> files;
    private boolean isClient;
    private boolean isGroupOwner;
    private String groupOwnerAddress;

    public FileTransferService() {
        super("FileTransferService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        files = intent.getStringArrayListExtra(FILE_ARRAY);
        isClient = intent.getBooleanExtra(IS_Client, false);
        isGroupOwner = intent.getBooleanExtra(IS_GROUP_OWNER, false);
        groupOwnerAddress = intent.getStringExtra(GROUP_OWNER_ADDRESS);

        progressShow();

        boolean ipFlag = false;
        if (isClient) {
            //发送文件
            if (isGroupOwner) {
                Log.i(TAG, "onHandleIntent: client+groupowner");
                //发送文件方若为groupOwner，则需要接受文件方将自己地址发送过来
                ServerSocket server = null;
                Socket socket = null;
                String ip = null;
                try {
                    server = new ServerSocket(IP_TRANSFER_PORT);
                    socket = server.accept();

                    ip = socket.getInetAddress().getHostAddress();

                    ipFlag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    try {
                        if (server != null && !server.isClosed()) server.close();
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (ipFlag) {
                        sendFile(ip);
                    }
                }

            } else {
                Log.i(TAG, "onHandleIntent: client + peer");
                sendFile(groupOwnerAddress);
            }

        } else {
            //接受文件
            if (!isGroupOwner) {
                Log.i(TAG, "onHandleIntent: server + peer");

                //接受文件方若不为groupOwner则需将ip地址发给发送文件方
                Socket socket = null;
                OutputStream os = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;
                try {
                    socket = new Socket(groupOwnerAddress, IP_TRANSFER_PORT);
                    os = socket.getOutputStream();
                    osw = new OutputStreamWriter(os);
                    bw = new BufferedWriter(osw);
                    bw.write("Hello");

                    ipFlag = true;

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) bw.close();
                        if (osw != null) osw.close();
                        if (os != null) os.close();
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (ipFlag) {
                        receiveFile();
                    }
                }


            } else {
                Log.i(TAG, "onHandleIntent: server + groupOwner");

                receiveFile();
            }
        }
    }

    private void sendFile(String ip) {
        Log.i(TAG, "sendFile: send file");
        Log.i(TAG, "sendFile: filesize " + files.size());
        Log.i(TAG, "sendFile: "+Environment.getExternalStorageDirectory());

        int count=0;
        //发送文件，每个文件的发送都会发起一个socket连接
        for (String filePath : files) {
            filePath=filePath.substring(5);

            File file = new File(filePath);
            Log.i(TAG, "sendFile: "+filePath + " " +file.length() +" "+file.exists());

            Socket socket = null;
            DataOutputStream dos = null;
            FileInputStream fis = null;

            if (file.exists()) {
                try {
                    Log.i(TAG, "sendFile: new Socket");
                    socket = new Socket(ip, FILE_TRANSFER_PORT);
                    dos = new DataOutputStream(socket.getOutputStream());
                    fis = new FileInputStream(file);

                    dos.writeUTF(file.getName());
                    dos.flush();
                    dos.writeLong(file.length());
                    dos.flush();

                    Log.i(TAG, "sendFile: " + file.getName());

                    byte[] bytes = new byte[1024];
                    int length = 0;
                    long progress = 0;
                    long sendLength=0;

                    count++;
                    progressNewFile(count,file.getName());

                    long preProgress=0;
                    while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                        dos.write(bytes, 0, length);
                        dos.flush();

                        sendLength+=length;
                        progress = sendLength*100/file.length();

                        if(progress-preProgress >= 1) {
                            Log.i(TAG, "receiveFile: update progress["+progress+"] length["+length+"]"+"fileLength ["+file.length()+"]");

                            progressUpdate((int) progress);
                            preProgress=progress;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(fis != null)
                             fis.close();
                        if(dos != null)
                             dos.close();
                        if(socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        Log.i(TAG, "sendFile: fileTransferEnd");
        //文件传送结束，通知关闭文件接收端
        Socket socket = null;
        DataOutputStream dos = null;
        try {
            socket = new Socket(ip, FILE_TRANSFER_PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(FILE_TRANSFER_END);
            dos.writeLong(-1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(dos != null)
                dos.close();
                if(socket != null)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        progressClose(count);
    }

    private void receiveFile() {
        Log.i(TAG, "receiveFile: receivefile");

        int count=0;

        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(FILE_TRANSFER_PORT));

            while (true) {
                Log.i(TAG, "receiveFile: Server open accepting");
                socket = server.accept();
                Log.i(TAG, "receiveFile: accepted");

                DataInputStream dis = new DataInputStream(socket.getInputStream());

                String fileName = dis.readUTF();
                long fileLength = dis.readLong();

                Log.i(TAG, "receiveFile: " + fileName);

                //文件发送方传送文件结束，可以关闭客户端了
                if (fileName.equals(FILE_TRANSFER_END) && fileLength == -1) {
                    dis.close();
                    socket.close();
                    break;
                }

                //接收文件
                String savePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/"+fileName;
                File file=new File(savePath);
                if(!file.createNewFile()) Log.i(TAG, "receiveFile: cerated File");


                FileOutputStream fos = new FileOutputStream(file);

                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                long receiveLength=0;

                count++;
                progressNewFile(count,fileName);

                long preProgress=0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes);
                    fos.flush();

                    receiveLength+=length;
                    progress = receiveLength * 100 / fileLength;

                    //reduce the number of the sending of broadcast
                    if(progress-preProgress >=1) {
                        Log.i(TAG, "receiveFile: update progress["+progress+"] length["+length+"]");

                        progressUpdate((int) progress);
                        preProgress=progress;
                    }
                }

                Log.i(TAG, "receiveFile: "+savePath+" "+file.exists());
                Log.i(TAG, "receiveFile: "+count);
                dis.close();
                fos.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (server != null && !server.isClosed()) server.close();
                if (socket != null && socket.isConnected()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        progressClose(count);

    }


    private void progressShow(){
        Intent i=new Intent(FileShareActivity.PROGRESS_SHOW);
        sendBroadcast(i);
    }

    private void progressNewFile(int count,String name){
        Intent i=new Intent(FileShareActivity.PROGRESS_UPDATE);
        i.putExtra("COUNT",count);
        i.putExtra("PROGRESS",0);
        i.putExtra("NEWFILE",true);
        i.putExtra("FILENAME",name);
        sendBroadcast(i);
    }

    private void progressUpdate(int progress){
        Intent i=new Intent(FileShareActivity.PROGRESS_UPDATE);
        i.putExtra("NEWFILE",false);
        i.putExtra("PROGRESS",progress);
        sendBroadcast(i);
    }

    private void progressClose(int count){
        Intent i=new Intent(FileShareActivity.PROGRESS_CLOSE);
        i.putExtra("COUNT",count);
        sendBroadcast(i);
    }
}
