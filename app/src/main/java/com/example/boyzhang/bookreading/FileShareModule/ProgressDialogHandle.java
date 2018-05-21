package com.example.boyzhang.bookreading.FileShareModule;

import android.content.Intent;

/**
 * Created by LunHui on 2018/5/7.
 */

public interface ProgressDialogHandle {
    void showProgressDialog(Intent intent);
    void updateProgressDialog(Intent intent);
    void closeProgressDialog(Intent intent);
}
