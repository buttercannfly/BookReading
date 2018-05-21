package com.example.boyzhang.bookreading.FileShareModule;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LunHui on 2018/5/8.
 */

public class WifiP2pOperator implements WifiP2pManager.ChannelListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.PeerListListener {

    private static final String TAG = "WifiP2pOperator";

    private Notifier mNotifier = null;

    private boolean isConnected = false;
    private WifiP2pDevice connectedDevice=null;


    private boolean isOpen = false;

    private boolean isGroupOwner = false;
    private String groupOwnerIp = null;

    private Activity mActivity = null;
    private WifiP2pManager mManager = null;
    private WifiP2pManager.Channel mChannel = null;
    private WifiP2pBroadcastReceiver mBroadcast = null;

    private List<WifiP2pDevice> mDeviceList = new ArrayList<WifiP2pDevice>();


    public WifiP2pOperator(Activity a, Notifier n) {
        mActivity = a;
        mNotifier = n;
    }

    public void open() {
        mManager = (WifiP2pManager) mActivity.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(mActivity, mActivity.getMainLooper(), this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mBroadcast = new WifiP2pBroadcastReceiver(this, mManager, mChannel);
        mActivity.registerReceiver(mBroadcast, filter);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });

        isOpen = true;
    }


    public List<WifiP2pDevice> getDeviceList() {
        return mDeviceList;
    }

    public void connectWith(final WifiP2pDevice d) {

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = d.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                isConnected=true;
                connectedDevice=d;
            }

            @Override
            public void onFailure(int i) {
                isConnected=false;
                connectedDevice=null;
            }
        });

    }

    public boolean isGroupOwner() {
        return isGroupOwner;
    }

    public String getGroupOwnerIp() {
        return groupOwnerIp;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public boolean isConnected(){
        return isConnected;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void close() {

        Log.i(TAG, "close: ");

        if (mActivity != null)
            mActivity.unregisterReceiver(mBroadcast);

        if (mManager != null) {
            mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "onSuccess: removeGroup");
                }

                @Override
                public void onFailure(int i) {
                    Log.i(TAG, "onFailure: removeGroup");
                }
            });

            mManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "onSuccess: cancelConnect");
                }

                @Override
                public void onFailure(int i) {
                    Log.i(TAG, "onFailure: cancelConnect");
                }
            });
            mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "onSuccess: stopPeerDiscovery");
                }

                @Override
                public void onFailure(int i) {
                    Log.i(TAG, "onFailure: stopPeerDiscovery");
                }
            });
        }


        mManager = null;
        mChannel = null;

        isConnected = false;
        isGroupOwner = false;
        isOpen = false;

        mDeviceList.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void disconnect(){
        close();
        open();
    }

    @Override
    public void onChannelDisconnected() {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        if (wifiP2pInfo.groupFormed) {
            isGroupOwner = wifiP2pInfo.isGroupOwner;
            groupOwnerIp = wifiP2pInfo.groupOwnerAddress.getHostAddress();

            mNotifier.notifyConnected();
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        mDeviceList.clear();
        mDeviceList.addAll(wifiP2pDeviceList.getDeviceList());
        mNotifier.notifyPeersChanged();
    }


    public interface Notifier {
        void notifyConnected();

        void notifyPeersChanged();
    }
}
