package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class TireLiuchengActivity extends BaseActivity {
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_liucheng,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("购买流程");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
    }
}
