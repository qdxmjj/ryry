package com.ruyiruyi.rylibrary.base;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public abstract class BaseActivity extends AppCompatActivity {


    private ActionBar a;

    public BaseActivity(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            return;
        }else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_primary_top));
        }
    }

    protected void setContentView(int var1,int var2){
        this.setContentView(var1);
        if (var2 != -1){
            a = ((ActionBar) this.findViewById(var2));
            a.setBackgroundColor(getResources().getColor(R.color.theme_primary));
        }
    }

    protected void setActionBarTitle(ActionBar var1, String var2){
        if (var1 != null){
            var1.setTitle(var2);
        }
    }

}
