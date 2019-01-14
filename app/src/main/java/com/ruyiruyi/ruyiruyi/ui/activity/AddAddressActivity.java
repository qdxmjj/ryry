package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Province;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class AddAddressActivity extends RyBaseActivity {

    private ActionBar actionBar;
    private EditText et_name;
    private EditText et_phone;
    private TextView tv_address;
    private EditText et_address;
    private Switch switch_address;
    private WheelView whv_sheng, whv_shi, whv_xian;
    private TextView tv_city_right;

    private int id;
    private String name;
    private String phone;
    private String addressTxt;//填写的详细地址
    private String sheng = "";
    private String shi = "";
    private String xian = "";
    private boolean isDefault;//是否设为默认地址
    private String TAG = AddAddressActivity.class.getSimpleName();
    private List<String> shengList;
    private List<String> shiList;
    private List<String> xianList;
    public String currentSheng = "北京市";
    public String currentShi = "北京市";
    public String currentXian = "东城区";
    public int currentShengPosition = 0;
    public int currentShiPosition = 0;
    public int currentXianPosition = 0;
    private String whereIn = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        Intent intent = getIntent();
        whereIn = intent.getStringExtra("whereIn");

        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle(whereIn.equals("reedit") ? "编辑收货地址" : "添加收货地址");
        actionBar.setRightView("完成");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        //完成
                        onFinishClick();
                        break;
                }
            }
        });


        initView();
        initData();
        bindView();


    }

    private void initData() {
        getSheng();
        getShi();
        getXian();

        //编辑跳转进入 则获取原地址数据
        if (whereIn.equals("reedit")) {
            Bundle extras = getIntent().getExtras();
            id = extras.getInt("id");
            name = extras.getString("name");
            phone = extras.getString("phone");
            sheng = extras.getString("sheng");
            shi = extras.getString("shi");
            xian = extras.getString("qu");
            addressTxt = extras.getString("address").replace(sheng + shi + xian, "");
            int anInt = extras.getInt("isDefault", 0);
            isDefault = anInt == 1 ? true : false;
            et_name.setText(name);
            et_phone.setText(phone);
            tv_address.setText(sheng + " " + shi + " " + xian + " ");
            et_address.setText(addressTxt);
            switch_address.setChecked(isDefault);
        }
    }

    /**
     * 添加收货地址完成
     */
    private void onFinishClick() {
        if (!judgeBeforeFinish()) {
            //条件不满足
            return;
        }
        //条件满足
        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        addressTxt = et_address.getText().toString();
        isDefault = switch_address.isChecked();

        if (whereIn.equals("reedit")) {//返回地址列表并刷新
            //收货地址管理页面 编辑地址 提交
            RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/address");
            params.addBodyParameter("userId", new DbConfig(getApplicationContext()).getId() + "");
            params.addBodyParameter("id", id + "");
            params.addBodyParameter("name", name);
            params.addBodyParameter("phone", phone);
            params.addBodyParameter("address", sheng + shi + xian + addressTxt);
            params.addBodyParameter("isDefault", isDefault ? "1" : "0");//是否默认地址[0:否,1:是]
            params.addBodyParameter("province", sheng);
            params.addBodyParameter("city", shi);
            params.addBodyParameter("county", xian);
            x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        String msg = jsonObject.getString("msg");

                        if (status == 1) {
                            //  编辑结束 保存后 返回地址列表并刷新
                            finish();
                        }
                        Toast.makeText(AddAddressActivity.this, msg, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(AddAddressActivity.this, "地址编辑失败,请检查网络", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        } else if (whereIn.equals("listAdd")) {//返回地址列表并刷新
            //地址列表 空Item点击添加地址 提交
            RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/address");
            params.addBodyParameter("userId", new DbConfig(getApplicationContext()).getId() + "");
            params.addBodyParameter("name", name);
            params.addBodyParameter("phone", phone);
            params.addBodyParameter("address", sheng + shi + xian + addressTxt);
            params.addBodyParameter("isDefault", isDefault ? "1" : "0");//是否默认地址[0:否,1:是]
            params.addBodyParameter("province", sheng);
            params.addBodyParameter("city", shi);
            params.addBodyParameter("county", xian);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        String msg = jsonObject.getString("msg");

                        if (status == 1) {
                            // 返回地址列表并刷新
                            finish();

                        } else {
                            //操作失败后
                            Toast.makeText(AddAddressActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(AddAddressActivity.this, "地址保存失败,请检查网络", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }


    }

    private boolean judgeBeforeFinish() {
        if (et_name.getText() == null || et_name.getText().length() == 0) {
            Toast.makeText(this, "请填写姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_phone.getText() == null || et_phone.getText().length() == 0) {
            Toast.makeText(this, "请填写联系电话", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sheng == null || sheng.length() == 0) {
            Toast.makeText(this, "请选择省、市、区", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_address.getText() == null || et_address.getText().length() == 0) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void bindView() {
        RxViewAction.clickNoDouble(tv_address).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                View v_city = LayoutInflater.from(AddAddressActivity.this).inflate(R.layout.dialog_city_view, null);
                whv_sheng = (WheelView) v_city.findViewById(R.id.whview_city_sheng);
                whv_shi = (WheelView) v_city.findViewById(R.id.whview_city_shi);
                whv_xian = (WheelView) v_city.findViewById(R.id.whview_city_xian);
                tv_city_right = (TextView) v_city.findViewById(R.id.tv_city_right);

                whv_sheng.setItems(shengList, currentShengPosition);//初始0
                whv_sheng.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentShengPosition = whv_sheng.getSelectedPosition();
                        currentSheng = whv_sheng.getSelectedItem();
                        getShi();
                        whv_shi.setItems(shiList, currentShiPosition);
                        currentShi = whv_shi.getSelectedItem();
                        getXian();
                        whv_xian.setItems(xianList, currentXianPosition);
                    }
                });

                whv_shi.setItems(shiList, currentShiPosition);
                whv_shi.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentShiPosition = whv_shi.getSelectedPosition();
                        currentShi = whv_shi.getSelectedItem();
                        getXian();
                        whv_xian.setItems(xianList, currentXianPosition);
                    }
                });
                whv_xian.setItems(xianList, currentXianPosition);
                whv_xian.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentXianPosition = whv_xian.getSelectedPosition();
                    }
                });
                whv_sheng.setIsLoop(false);
                whv_shi.setIsLoop(false);
                whv_xian.setIsLoop(false);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddAddressActivity.this);
                bottomSheetDialog.setCancelable(false);

                RxViewAction.clickNoDouble(tv_city_right).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        sheng = whv_sheng.getSelectedItem();
                        shi = whv_shi.getSelectedItem();
                        xian = whv_xian.getSelectedItem();
                        String areStr = "";
                        if (xian.equals("无")) {
                            xian = "";
                            areStr = sheng + shi;
                        } else {
                            areStr = sheng + shi + xian;
                        }
                        tv_address.setText(areStr + " ");
                        bottomSheetDialog.cancel();
                    }
                });

                bottomSheetDialog.setContentView(v_city);
                //设置底部白色背景为透明
                bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(AddAddressActivity.this.getResources().getColor(R.color.transparent));
                bottomSheetDialog.show();

            }
        });
    }


    private void getSheng() {
        shengList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList = db.selector(Province.class)
                    .where("definition", "=", 1)
                    .findAll();
        } catch (DbException e) {

        }
        if (provinceList != null) {
            for (int i = 0; i < provinceList.size(); i++) {
                shengList.add(provinceList.get(i).getName());
            }
        }
    }


    private void getShi() {
        shiList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        List<Province> provinceList = new ArrayList<>();
        Log.e(TAG, "getShi: " + currentSheng);
        try {
            provinceList = db.selector(Province.class)
                    .where("name", "=", currentSheng)
                    .findAll();
        } catch (DbException e) {
        }
        if (provinceList != null) {
            if (provinceList.size() == 0) {
                shiList.add("无");
            } else {

            }
            Log.e(TAG, "getShi: " + provinceList.size());
            int shengId = provinceList.get(0).getId();
            List<Province> shiAllList = new ArrayList<>();
            try {
                shiAllList = db.selector(Province.class)
                        .where("fid", "=", shengId)
                        .findAll();
            } catch (DbException e) {
            }
            for (int i = 0; i < shiAllList.size(); i++) {
                shiList.add(shiAllList.get(i).getName());
            }
        }

    }

    private void getXian() {
        xianList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList = db.selector(Province.class)
                    .where("name", "=", currentShi)
                    .findAll();
        } catch (DbException e) {
        }
        if (provinceList != null) {
            if (provinceList.size() == 0) {
                xianList.add("无");
            } else {
                int shiId = provinceList.get(0).getId();
                List<Province> xianAllList = new ArrayList<>();
                try {
                    xianAllList = db.selector(Province.class)
                            .where("fid", "=", shiId)
                            .findAll();
                } catch (DbException e) {
                }

                for (int i = 0; i < xianAllList.size(); i++) {
                    xianList.add(xianAllList.get(i).getName());
                }
            }
        }
    }


    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        et_address = (EditText) findViewById(R.id.et_address);
        switch_address = (Switch) findViewById(R.id.switch_address);

        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        xianList = new ArrayList<>();
    }
}
