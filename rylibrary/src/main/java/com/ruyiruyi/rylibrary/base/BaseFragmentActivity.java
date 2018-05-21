package com.ruyiruyi.rylibrary.base;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

public class BaseFragmentActivity extends FragmentActivity {
    

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }

    public void hideDialogProgress(ProgressDialog dialog) {
        dialog.dismiss();
    }
}