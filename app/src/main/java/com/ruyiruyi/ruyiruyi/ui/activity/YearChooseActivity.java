package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.cell.TSeekBar;

public class YearChooseActivity extends AppCompatActivity {

    private TSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_choose);

        seekBar = (TSeekBar) findViewById(R.id.seekBar);

        seekBar.setMax(8);
    }
}
