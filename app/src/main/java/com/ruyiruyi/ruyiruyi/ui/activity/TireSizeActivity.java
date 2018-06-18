package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.TireType;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class TireSizeActivity extends RyBaseActivity {
    private static final String TAG = TireSizeActivity.class.getSimpleName();
    private ActionBar actionBar;
    private WheelView widthWV;
    private WheelView diameterWV;
    private WheelView radioWv;
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    private int weizhi = 0; //0是前轮 1是后轮
    public static boolean ishave = false;
    private List<String> widthList;
    private List<String> diameterList;
    private List<String> ratiodList;
    public static String currenWidth = "";
    public static int currenWidthPositoin = 1;
    public static String currendDiameter = "";
    public static int currendDiameterPosition = 1;
    public static String currenRadio = "";
    public static int currenRadioPosition = 1;
    private TextView saveTireButton;
    private TextView weizhiText;
    private String tire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_size,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("轮胎规格选择");;
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

        Intent intent = getIntent();
        weizhi = intent.getIntExtra("WEIZHI",0);
        tire = intent.getStringExtra("TIRE");
        if (!tire.isEmpty()){
            int one = tire.indexOf("/");
            int two = tire.indexOf("R");
            String width = tire.substring(0, one);
            String radio = tire.substring(one+1, two);
            String diameter = tire.substring(two+1, tire.length());
            currenWidth = width;
            currenRadio = radio;
            currendDiameter = diameter;

            Log.e(TAG, "onCreate: " + currenWidth);
            Log.e(TAG, "onCreate: " + currenRadio);
            Log.e(TAG, "onCreate: " + currendDiameter);

        }

        initView();


    }

    private void initView() {
        widthWV = (WheelView) findViewById(R.id.width_wheel_view);
        diameterWV = (WheelView) findViewById(R.id.diameter_wheel_view);
        radioWv = (WheelView) findViewById(R.id.radiowheel_view);
        saveTireButton = (TextView) findViewById(R.id.save_tire);
        weizhiText = (TextView) findViewById(R.id.weizhi_text);

        if (weizhi == 0){
            weizhiText.setText("前轮尺寸");
        }else {
            weizhiText.setText("后轮尺寸");
        }

        RxViewAction.clickNoDouble(saveTireButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String width = widthWV.getSelectedItem();
                        String diameter = diameterWV.getSelectedItem();
                        String radio = radioWv.getSelectedItem();
                        Intent intent = new Intent();
                        intent.putExtra("weizhi",weizhi);
                        intent.putExtra("tire",width + "/" + radio+ "R" + diameter);
                        setResult(CarInfoActivity.TIRE_SIZE, intent);
                        finish();

                    }
                });

        getWidth();
        getRatio(); //扁平比
        getDiameter();//直径


        initWv();




    }

    private void initWv() {
        widthWV.setItems(widthList,currenWidthPositoin);//init selected position is 1 初始选中位置为1
        widthWV.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currenWidthPositoin = widthWV.getSelectedPosition();
                String width = widthWV.getSelectedItem();
                currenWidth = width;
                getRatio();
                getDiameter();
                radioWv.setItems(ratiodList,1);
                diameterWV.setItems(diameterList,1);


            }

        });
        Log.e(TAG, "onItemSelected:-+++" + widthWV.getSelectedItem());


        radioWv.setItems(ratiodList,currenRadioPosition);//init selected position is 1 初始选中位置为1
        radioWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currenRadioPosition = radioWv.getSelectedPosition();
                String radio = radioWv.getSelectedItem();
                currenRadio = radio;
                getDiameter();
                diameterWV.setItems(diameterList,currendDiameterPosition);
            }

        });


        diameterWV.setItems(diameterList,currendDiameterPosition);//init selected position is 1 初始选中位置为1
        diameterWV.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currendDiameterPosition = diameterWV.getSelectedPosition();
            }

        });



    }


    private void getWidth() {
        //从数据库中获取轮胎型号
        DbManager db = new DbConfig(this).getDbManager();
        List<TireType> tireType = new ArrayList<>();
        try {
            tireType  = db.selector(TireType.class)
                    .findAll();
        } catch (DbException e) {

        }
        List<String> widthAllList = new ArrayList<>();
        Log.e(TAG, "initView: " + tireType.size());
        for (int i = 0; i < tireType.size(); i++) {
            widthAllList.add(tireType.get(i).getTireFlatWidth().toString());
        }
       widthList = new ArrayList<>(); //没重复的width
        //去除重复的轮胎型号
        for (int i = 0; i < widthAllList.size(); i++) {
            String wisthStr = widthAllList.get(i).toString();
            ishave =false;
            for (int j = 0; j < widthList.size(); j++) {        //去重
                if (wisthStr.equals(widthList.get(j).toString())){
                    ishave  = true;//是否是重复数据
                }
            }
            if (!ishave){

                widthList.add(wisthStr);
            }
        }

        for (int i = 0; i < widthList.size(); i++) {
            if (currenWidth.equals(widthList.get(i).toString())) {
                currenWidthPositoin = i;
            }
        }


    }

    private void getDiameter() {
        DbManager db = new DbConfig(this).getDbManager();
        List<TireType> tireType = new ArrayList<>();
        try {
            tireType  = db.selector(TireType.class)
                    .where("tireflatwidth" ,"=",currenWidth)
                    .where("tireFlatnessRatio" ,"=",currenRadio)
                    .findAll();
        } catch (DbException e) {

        }
        List<String> diameterAllList = new ArrayList<>();
        Log.e(TAG, "initView: " + tireType.size());
        for (int i = 0; i < tireType.size(); i++) {
            diameterAllList.add(tireType.get(i).getTireDiameter().toString());
        }
        diameterList = new ArrayList<>();
        //去除重复的轮胎型号
        for (int i = 0; i < diameterAllList.size(); i++) {
            String wisthStr = diameterAllList.get(i).toString();
            ishave =false;
            for (int j = 0; j < diameterList.size(); j++) {        //去重
                if (wisthStr.equals(diameterList.get(j).toString())){
                    ishave  = true;//是否是重复数据
                }
            }
            if (!ishave){

                diameterList.add(wisthStr);
            }
        }
        for (int i = 0; i < diameterList.size(); i++) {
            if (currendDiameter.equals(diameterList.get(i).toString())) {
                currendDiameterPosition = i;
            }
        }


    }

    private void getRatio() {
        //从数据库中获取轮胎型号
        DbManager db = new DbConfig(this).getDbManager();
        List<TireType> tireType = new ArrayList<>();
        try {
            tireType  = db.selector(TireType.class)
                    .where("tireflatwidth" ,"=",currenWidth)
                    .findAll();
        } catch (DbException e) {

        }
        Log.e(TAG, "getRatio:--------------------- " + tireType.size());
        List<String> radioAllList = new ArrayList<>();

        for (int i = 0; i < tireType.size(); i++) {
            radioAllList.add(tireType.get(i).getTireFlatnessRatio().toString());
        }
       ratiodList = new ArrayList<>();
        //去除重复的轮胎型号
        for (int i = 0; i < radioAllList.size(); i++) {
            String wisthStr = radioAllList.get(i).toString();
            ishave =false;
            for (int j = 0; j < ratiodList.size(); j++) {        //去重
                if (wisthStr.equals(ratiodList.get(j).toString())){
                    ishave  = true;//是否是重复数据
                }
            }
            if (!ishave){

                ratiodList.add(wisthStr);
            }
        }

        for (int i = 0; i < ratiodList.size(); i++) {
            if (currenRadio.equals(ratiodList.get(i).toString())) {
                currenRadioPosition = i;
            }
        }

        Log.e(TAG, "getRatio: ++++++++++"+ratiodList.size() );
    }

}
