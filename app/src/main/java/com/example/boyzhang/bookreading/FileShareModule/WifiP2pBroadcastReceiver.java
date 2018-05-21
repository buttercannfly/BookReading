package com.example.boyzhang.bookreading.FileShareModule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class WifiP2pBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "WifiP2pBroadcastReceiv";
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pOperator mOperator;



    public WifiP2pBroadcastReceiver(){
    }

    public WifiP2pBroadcastReceiver(WifiP2pOperator op, WifiP2pManager m, WifiP2pManager.Channel c){
        mOperator=op;
        mManager=m;
        mChannel=c;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action=intent.getAction();

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            Log.i(TAG, "onReceive: state changed");
            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                //do nothing
                Log.i(TAG, "onReceive: state enable");
            }else{
                //wifip2p is disable
                Log.i(TAG, "onReceive: state disable");
            }

        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            Log.i(TAG, "onReceive: peers changed");

            if(mManager != null) {
                Log.i(TAG, "onReceive: request peers");
                mManager.requestPeers(mChannel, mOperator);
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            Log.i(TAG, "onReceive: connection changed");

            if(mManager == null)return;

            NetworkInfo networkInfo=(NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected()){
                Log.i(TAG, "onReceive: request connection");
                mManager.requestConnectionInfo(mChannel,mOperator);
            }

        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            Log.i(TAG, "onReceive: this device changed");
        }

        //用于更新进度条

    }
}
