package com.example.boyzhang.bookreading.FileShareModule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TransferBroadcastReceiver extends BroadcastReceiver {

    private ProgressDialogHandle mProgressHandle;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action=intent.getAction();

        if(action.equals(FileShareActivity.PROGRESS_SHOW)){
            mProgressHandle.showProgressDialog(intent);
        }else if(action.equals(FileShareActivity.PROGRESS_UPDATE)){
            mProgressHandle.updateProgressDialog(intent);
        }else if(action.equals(FileShareActivity.PROGRESS_CLOSE)){
            mProgressHandle.closeProgressDialog(intent);
        }
    }

    public void setProgressHandle(ProgressDialogHandle p){
        mProgressHandle=p;
    }
}
