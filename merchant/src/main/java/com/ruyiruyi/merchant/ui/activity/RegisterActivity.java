package com.ruyiruyi.merchant.ui.activity;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.XiangmusBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.Province;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import rx.functions.Action1;

public class RegisterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private EditText et_userName;
    private EditText et_userPhone;
    private TextView tv_getCode;
    private EditText et_Code;
    private ImageView img_CodeRight;
    private EditText et_Passworda;
    private EditText et_Passwordb;
    private EditText et_Shopname;
    private TextView tv_ShopCategory;
    private EditText et_ShopPhone;
    private TextView tv_ShopTime;
    private TextView tv_ShopCity;
    private EditText et_ShopLocation;
    private TextView tv_ShopPoint;
    private ImageView img_yyzz;
    private ImageView img_yyzz_delete;
    private ImageView img_mdpic_a;
    private ImageView img_mdpic_a_delete;
    private ImageView img_mdpic_b;
    private ImageView img_mdpic_b_delete;
    private ImageView img_mdpic_c;
    private ImageView img_mdpic_c_delete;
    private ImageView img_shou_a;
    private ImageView img_shou_a_delete;
    private CheckBox ckbox_a;
    private CheckBox ckbox_b;
    private CheckBox ckbox_c;
    private CheckBox ckbox_d;
    private CheckBox ckbox_e;
    private CheckBox ckbox_f;
    private CheckBox ckbox_sl;
    private CheckBox ckbox_bsl;
    private TextView tv_save;
    private WheelView whv_sheng, whv_shi, whv_xian;
    private Spinner spn_category;
    //test !!!
    private LinearLayout ll_xms;
    private List<String> list_xms = new ArrayList<>();

    private ActionBar mActionBar;
    private TimeCount mTimeCount;
    private String TAG = RegisterActivity.class.getSimpleName();
    private String user_name, user_phone, code, password_a, password_b, shop_name, shop_phone, shop_location;
    private List<String> shengList;
    private List<String> shiList;
    private List<String> xianList;
    public String currentSheng = "北京市";
    public String currentShi = "北京市";
    public String currentXian = "东城区";
    public int currentShengPosition = 0;
    public int currentShiPosition = 0;
    public int currentXianPosition = 0;
    private int areaId = 0;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mActionBar = (ActionBar) findViewById(R.id.acbar_rigister);
        mActionBar.setTitle("门店注册");
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
        initProvinceData();
        initView();
        textAddXMView();
        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        xianList = new ArrayList<>();

        getSheng();
        getShi();
        getXian();
        bindView();
    }

    private void textAddXMView() {
        for (int i = 0; i < 10; i++) {
            list_xms.add("轮胎服务" + i);
        }
        ll_xms = (LinearLayout) findViewById(R.id.ll_xiangmus);
        for (int i = 0; i < (list_xms.size() % 3 == 0 ? (list_xms.size() / 3) : (list_xms.size() / 3 + 1)); i++) {
            View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.register_xm_add_item, null);
            CheckBox checkBoxa = (CheckBox) view.findViewById(R.id.checkbox_a);
            CheckBox checkBoxb = (CheckBox) view.findViewById(R.id.checkbox_b);
            CheckBox checkBoxc = (CheckBox) view.findViewById(R.id.checkbox_c);
            checkBoxa.setText(list_xms.get(i * 3 + 0));
            checkBoxa.setTag(new XiangmusBean(i * 3 + 0, list_xms.get(i * 3 + 0)));
            checkBoxa.setOnCheckedChangeListener(this);

            checkBoxb.setText((i * 3 + 1 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 1));
            checkBoxb.setVisibility((i * 3 + 1 >= list_xms.size()) ? View.GONE : View.VISIBLE);
            checkBoxb.setTag(new XiangmusBean(i * 3 + 1, (i * 3 + 1 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 1)));
            checkBoxb.setOnCheckedChangeListener(this);

            checkBoxc.setText((i * 3 + 2 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 2));
            checkBoxc.setVisibility((i * 3 + 2 >= list_xms.size()) ? View.GONE : View.VISIBLE);
            checkBoxc.setTag(new XiangmusBean(i * 3 + 2, (i * 3 + 2 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 2)));
            checkBoxc.setOnCheckedChangeListener(this);

            ll_xms.addView(view);
        }
    }

    private void initProvinceData() {
        date = new Date();
        List<Province> provinceList = null;
        try {
            provinceList = new DbConfig().getDbManager().selector(Province.class).orderBy("time").findAll();
        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();
        try {
            //时间请求 ！！
            if (provinceList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time", time);
            }

        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(UtilsURL.LOGIN_PASS_REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initProvinceData: params = " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.e(TAG, "onSuccess: getData and To Db ??  status = " + status + "msg = " + msg + "data = " + data.toString());
                    saveProvinceToDb(data);
                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: getData and To Db ?? ");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: getData and To Db ??");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveProvinceToDb(JSONArray data) {
        Province province;
        DbManager db = (new DbConfig()).getDbManager();
        for (int i = 0; i < data.length(); i++) {
            province = new Province();
            try {
                JSONObject obj = (JSONObject) data.get(i);
                province.setDefinition(obj.getInt("definition"));
                province.setFid(obj.getInt("fid"));
                province.setId(obj.getInt("id"));
                province.setName(obj.getString("name"));
                Log.e(TAG, "saveProvinceToDb: definition==>" + province.getDefinition());
                db.saveOrUpdate(province);
            } catch (JSONException e) {

            } catch (DbException e) {

            }
        }
    }

    private void bindView() {
        RxViewAction.clickNoDouble(tv_getCode)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = et_userPhone.getText().toString();
                        if (phone.isEmpty()) {
                            showDialog("手机号不能为空");
                        } else if (!UtilsRY.isMobile(phone)) {
                            showDialog("手机号格式错误");
                        } else {
                            getCode(phone);
                        }
                    }
                });
        et_Code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Code.getText().length() == 6) {
                    img_CodeRight.setImageResource(R.drawable.ic_right);
                    img_CodeRight.setVisibility(View.VISIBLE);
                } else if (et_Code.getText().length() == 0) {
                    img_CodeRight.setVisibility(View.GONE);
                } else {
                    img_CodeRight.setImageResource(R.drawable.ic_wrong);
                    img_CodeRight.setVisibility(View.VISIBLE);
                }
            }
        });

        RxViewAction.clickNoDouble(tv_ShopCategory)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
    }

    public void registerclick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopcategory:
                View v_category = LayoutInflater.from(this).inflate(R.layout.dialog_category_view, null);
                spn_category = (Spinner) v_category.findViewById(R.id.spn_category);
//                spn_category.
                break;
            case R.id.tv_shoptime:
                break;
            case R.id.tv_shopcity:
                View v_city = LayoutInflater.from(this).inflate(R.layout.dialog_city_view, null);
                whv_sheng = (WheelView) v_city.findViewById(R.id.whview_city_sheng);
                whv_shi = (WheelView) v_city.findViewById(R.id.whview_city_shi);
                whv_xian = (WheelView) v_city.findViewById(R.id.whview_city_xian);

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
                new AlertDialog.Builder(this)
                        .setTitle("选择所在城市")
                        .setView(v_city)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String areStr = "";
                                String sheng = whv_sheng.getSelectedItem();
                                String shi = whv_shi.getSelectedItem();
                                String xian = whv_xian.getSelectedItem();
                                if (xian.equals("无")) {
                                    areStr = sheng + shi;
                                } else {
                                    areStr = sheng + shi + xian;
                                }
                                Log.e(TAG, "onClick: areStr = " + areStr);
                                tv_ShopCity.setText(areStr);

                                if (xian.equals("无") || xian.equals("")) {
                                    if (shi.equals("无") || shi.equals("")) {//没有市 没有县  获取省Id
                                        DbManager db = new DbConfig().getDbManager();
                                        List<Province> provinceList = new ArrayList<>();
                                        try {
                                            provinceList = db.selector(Province.class)
                                                    .where("name", "=", sheng)
                                                    .findAll();
                                        } catch (DbException e) {
                                        }
                                        if (provinceList != null) {
                                            areaId = provinceList.get(0).getId();
                                        }
                                    } else {     //没有县获取市id
                                        DbManager db = new DbConfig().getDbManager();
                                        List<Province> provinceList = new ArrayList<>();
                                        try {
                                            provinceList = db.selector(Province.class)
                                                    .where("name", "=", shi)
                                                    .findAll();
                                        } catch (DbException e) {
                                        }
                                        if (provinceList != null) {
                                            areaId = provinceList.get(0).getId();
                                        }

                                    }
                                } else {//获取县id
                                    DbManager db = new DbConfig().getDbManager();
                                    List<Province> provinceList = new ArrayList<>();
                                    try {
                                        provinceList = db.selector(Province.class)
                                                .where("name", "=", xian)
                                                .findAll();
                                    } catch (DbException e) {
                                    }
                                    if (provinceList != null) {
                                        areaId = provinceList.get(0).getId();
                                    }
                                }
                            }
                        })
                        .show();
                break;
            case R.id.tv_shoppoint:
                break;
        }
    }

    private void getSheng() {
        shengList.clear();
        DbManager db = new DbConfig().getDbManager();
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
        DbManager db = new DbConfig().getDbManager();
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
        DbManager db = new DbConfig().getDbManager();
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


    private void getCode(String phoneNumber) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phoneNumber);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.LOGIN_PASS_REQUEST_URL + "sendMsg");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String status;

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    Log.e(TAG, "geyCode-->onSuccess: code_result ==>" + result.toString());
                    status = jsonObject1.getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        mTimeCount.start();
                    } else {
                        Toast.makeText(RegisterActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        tv_getCode.setText("重新发送");
                    }
                } catch (JSONException e) {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initView() {
        et_userName = (EditText) findViewById(R.id.et_person);
        et_userPhone = (EditText) findViewById(R.id.et_phone);
        tv_getCode = (TextView) findViewById(R.id.tv_getcode);
        et_Code = (EditText) findViewById(R.id.et_code);
        img_CodeRight = (ImageView) findViewById(R.id.img_code_right);
        et_Passworda = (EditText) findViewById(R.id.et_pass_a);
        et_Passwordb = (EditText) findViewById(R.id.et_pass_b);
        et_Shopname = (EditText) findViewById(R.id.et_shopname);
        tv_ShopCategory = (TextView) findViewById(R.id.tv_shopcategory);
        et_ShopPhone = (EditText) findViewById(R.id.et_shopphone);
        tv_ShopTime = (TextView) findViewById(R.id.tv_shoptime);
        tv_ShopCity = (TextView) findViewById(R.id.tv_shopcity);
        et_ShopLocation = (EditText) findViewById(R.id.et_shoplocation);
        tv_ShopPoint = (TextView) findViewById(R.id.tv_shoppoint);
        img_yyzz = (ImageView) findViewById(R.id.img_yyzz);
        img_yyzz_delete = (ImageView) findViewById(R.id.img_yyzz_delete);
        img_mdpic_a = (ImageView) findViewById(R.id.img_mdpic_a);
        img_mdpic_a_delete = (ImageView) findViewById(R.id.img_mdpic_a_delete);
        img_mdpic_b = (ImageView) findViewById(R.id.img_mdpic_b);
        img_mdpic_b_delete = (ImageView) findViewById(R.id.img_mdpic_b_delete);
        img_mdpic_c = (ImageView) findViewById(R.id.img_mdpic_c);
        img_mdpic_c_delete = (ImageView) findViewById(R.id.img_mdpic_c_delete);
        img_shou_a = (ImageView) findViewById(R.id.img_shou_a);
        img_shou_a_delete = (ImageView) findViewById(R.id.img_shou_a_delete);
        ckbox_a = (CheckBox) findViewById(R.id.checkbox_a);
        ckbox_b = (CheckBox) findViewById(R.id.checkbox_b);
        ckbox_c = (CheckBox) findViewById(R.id.checkbox_c);
//        ckbox_d = (CheckBox) findViewById(R.id.checkbox_d);
//        ckbox_e = (CheckBox) findViewById(R.id.checkbox_e);
//        ckbox_f = (CheckBox) findViewById(R.id.checkbox_f);
        ckbox_sl = (CheckBox) findViewById(R.id.checkbox_sl);
        ckbox_bsl = (CheckBox) findViewById(R.id.checkbox_bsl);
        tv_save = (TextView) findViewById(R.id.tv_save);
        mTimeCount = new TimeCount(60000, 1000);
    }

    private void showDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_head);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        XiangmusBean bean = (XiangmusBean) buttonView.getTag();
        Toast.makeText(this," id= "+bean.getId()+" text = "+bean.getText()+" ",Toast.LENGTH_SHORT).show();
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_getCode.setBackgroundResource(R.drawable.btn_primary_enable);
            tv_getCode.setClickable(false);
            tv_getCode.setText("(" + millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            tv_getCode.setText("重新获取");
            tv_getCode.setClickable(true);
            tv_getCode.setBackgroundResource(R.drawable.login_code_button);
        }
    }
}
