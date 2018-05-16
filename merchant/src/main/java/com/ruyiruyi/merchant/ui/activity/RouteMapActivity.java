package com.ruyiruyi.merchant.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.utils.OpenLocalMapUtil;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class RouteMapActivity extends BaseActivity {

    private ActionBar mActionBar;
    private String startLat;
    private String startLong;
    private String endLat;
    private String endLong;
    private String startName;
    private String endName;
    private String city_;

    private static String APP_NAME = "ryry";
    private static String SRC = "com.ruyiruyi.merchant";
    private String TAG = RouteMapActivity.class.getSimpleName();
    private boolean isOpened;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        //ActionBar
        mActionBar = (ActionBar) findViewById(R.id.acbar_);
        mActionBar.setTitle("路线规划");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

       /* Bundle bundle = getIntent().getExtras();
        startLat = bundle.getString("user_latitude");
        startLong = bundle.getString("user_longitude");
        endLat = bundle.getString("store_latitude");
        endLong = bundle.getString("store_longitude");
        endName = bundle.getString("storeName");
        startName = "我的位置";
        city_ = "青岛";*/


        if (OpenLocalMapUtil.isBaiduMapInstalled()) {
            // 驾车路线规划
            Intent i1 = new Intent();
            i1.setData(Uri.parse("baidumap://map/direction?origin=name:" + startName + "|latlng:" + startLat + "," + startLong + "&destination=name:" + endName + "|latlng:" + endLat + "," + endLong + "&mode=driving"));
            //  origin 起点(注意：坐标先纬度，后经度)
            //  destination 终点(注意：坐标先纬度，后经度)
            startActivity(i1);
            finish();
        } else {
            openWebMap(Double.parseDouble(startLat), Double.parseDouble(startLong), startName, Double.parseDouble(endLat), Double.parseDouble(endLong), endName, city_);
            finish();
        }


    }


    /**
     * 打开百度地图
     */
    private void openBaiduMap(double slat, double slon, String sname, double dlat, double dlon, String dname, String city) {
        if (OpenLocalMapUtil.isBaiduMapInstalled()) {
            try {
                String uri = OpenLocalMapUtil.getBaiduMapUri(String.valueOf(slat), String.valueOf(slon), sname,
                        String.valueOf(dlat), String.valueOf(dlon), dname, city, SRC);
                Intent intent = Intent.parseUri(uri, 0);
                startActivity(intent); //启动调用


                isOpened = true;
            } catch (Exception e) {
                isOpened = false;
                e.printStackTrace();
            }
        } else {
            isOpened = false;
        }
    }


    /**
     * 打开浏览器进行百度地图导航
     */
    private void openWebMap(double slat, double slon, String sname, double dlat, double dlon, String dname, String city) {
        Uri mapUri = Uri.parse(OpenLocalMapUtil.getWebBaiduMapUri(String.valueOf(slat), String.valueOf(slon), sname,
                String.valueOf(dlat), String.valueOf(dlon),
                dname, city, APP_NAME));
        Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
        startActivity(loction);
    }


}
