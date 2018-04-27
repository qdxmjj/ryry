package com.ruyiruyi.merchant.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.XiangmusBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.ServiceType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

import org.xutils.common.util.DensityUtil;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


public class StoreXiangQingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private String TAG = StoreXiangQingFragment.class.getSimpleName();


    private TextView tv_shoptime;
    private LinearLayout ll_xiangmus;
    private TextView tv_save;

    private TextView tv_shopname;
    private TextView tv_shopcategory;
    private TextView tv_shopphone;
    private TextView tv_shopcity;
    private TextView tv_shoplocation;
    private ImageView img_mdpic_a;
    private ImageView img_mdpic_b;
    private ImageView img_mdpic_c;

    private WheelView whv_lTime, whv_rTime;

    public String currentlTime = "8:00";
    public String currentrTime = "18:00";
    public int currentlTimePosition = 0;
    public int currentrTimePosition = 0;
    private List<String> lTime_list;
    private List<String> rTime_list;
    private String shopTimes;
    private List<XiangmusBean> list_xms = new ArrayList<>();
    private List<String> serviceTypeList = new ArrayList<String>();
    private String serviceTypeListString;

    private String shopTimeL;
    private String shopTimeR;

    private String storeTimeL_old;
    private String storeTimeR_old;
    private List<String> storeServiceList_old = new ArrayList<>();
    private String storeName;
    private String storeCategory;
    private String storePhone;
    private String storeCity;
    private String storeLocation;
    private String mdPic_a_url;
    private String mdPic_b_url;
    private String mdPic_c_url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.store_xiangqing_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initView();
        bindView();
        getData();//暂用假数据 缺接口
        setData();

    }

    private void setData() {
        //控件预设数据
        tv_shoptime.setText(storeTimeL_old + " 至 " + storeTimeR_old);
        tv_shopname.setText(storeName);
        tv_shopcategory.setText(storeCategory);
        tv_shopphone.setText(storePhone);
        tv_shopcity.setText(storeCity);
        tv_shoplocation.setText(storeLocation);
/*        ImageOptions myOptions = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(2))
                .setCircular(false)
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_XY)
                .setSquare(true)// setSquare和build为必须设置
                .build();
        x.image().bind(img_mdpic_a, mdPic_a_url, myOptions);
        x.image().bind(img_mdpic_b, mdPic_b_url, myOptions);
        x.image().bind(img_mdpic_c, mdPic_c_url, myOptions);*/
        Glide.with(getActivity()).load(mdPic_a_url).into(img_mdpic_a);
        Glide.with(getActivity()).load(mdPic_b_url).into(img_mdpic_b);
        Glide.with(getActivity()).load(mdPic_c_url).into(img_mdpic_c);

    }

    private void getData() {
        storeServiceList_old.add("0");
        storeServiceList_old.add("3");
        storeTimeL_old = "08:00:00";
        storeTimeR_old = "21:00:00";
        storeName = "小马驾驾";
        storeCategory = "4S店";
        storePhone = "05328888888";
        storeCity = "山东省青岛市城阳区";
        storeLocation = "天安数码城23号楼";
        mdPic_a_url = "http://192.168.0.167/images/store/storeImg/18254262176/mdPica.png";
        mdPic_b_url = "http://192.168.0.167/images/store/storeImg/18254262176/mdPicb.png";
        mdPic_c_url = "http://180.76.243.205:8111/images/userHeadimgurl/defaultHead.png";

    }

    private void bindView() {
        //shopTime
        RxViewAction.clickNoDouble(tv_shoptime).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                View v_shoptime = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_shoptime_view, null);
                whv_lTime = (WheelView) v_shoptime.findViewById(R.id.whv_ltime);
                whv_rTime = (WheelView) v_shoptime.findViewById(R.id.whv_rtime);
                whv_lTime.setItems(getStrLTime(), currentlTimePosition);
                whv_rTime.setItems(getRTimeList(0), 0);
                whv_lTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentlTimePosition = whv_lTime.getSelectedPosition();
                        currentlTime = whv_lTime.getSelectedItem();
                        rTime_list = getRTimeList(selectedIndex);
                        whv_rTime.setItems(rTime_list, 0);
                        shopTimeL = item;
                        shopTimeR = rTime_list.get(0);
                        shopTimes = shopTimeL + "  至  " + shopTimeR;
                    }
                });
                whv_rTime.setItems(rTime_list, currentrTimePosition);
                whv_rTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentrTimePosition = whv_rTime.getSelectedPosition();
                        currentrTime = whv_rTime.getSelectedItem();
                        shopTimeR = item;
                        shopTimes = shopTimeL + "  至  " + shopTimeR;
                    }
                });
                new AlertDialog.Builder(getActivity())
                        .setTitle("选择营业时间")
                        .setView(v_shoptime)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_shoptime.setText(shopTimes);
                            }
                        }).show();
            }
        });
        //initXMs
        textAddXMView();
        //提交
        RxViewAction.clickNoDouble(tv_save).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (serviceTypeList == null || serviceTypeList.size() == 0) {
                    Toast.makeText(getActivity(), "请选择合作项目", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    serviceTypeListString = "";
                    for (int i = 0; i < serviceTypeList.size(); i++) {
                        if (i == serviceTypeList.size() - 1) {
                            serviceTypeListString = serviceTypeListString + serviceTypeList.get(i);
                        } else {
                            serviceTypeListString = serviceTypeListString + serviceTypeList.get(i) + ",";
                        }
                    }
                    Log.e(TAG, "call: XiangQing serviceTypeListString" + serviceTypeListString);
                }

            }
        });
        //门店照片 a
        RxViewAction.clickNoDouble(img_mdpic_a).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicDialog(mdPic_a_url);
            }
        });
        //门店照片 b
        RxViewAction.clickNoDouble(img_mdpic_b).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicDialog(mdPic_b_url);
            }
        });
        //门店照片 c
        RxViewAction.clickNoDouble(img_mdpic_c).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicDialog(mdPic_c_url);
            }
        });
    }

    private void initView() {
        //可修改的控件
        tv_shoptime = (TextView) getView().findViewById(R.id.tv_shoptime);
        tv_save = (TextView) getView().findViewById(R.id.tv_save);
        ll_xiangmus = (LinearLayout) getView().findViewById(R.id.ll_xiangmus);
        //不可修改的控件
        tv_shopname = (TextView) getView().findViewById(R.id.tv_shopname);
        tv_shopcategory = (TextView) getView().findViewById(R.id.tv_shopcategory);
        tv_shopphone = (TextView) getView().findViewById(R.id.tv_shopphone);
        tv_shopcity = (TextView) getView().findViewById(R.id.tv_shopcity);
        tv_shoplocation = (TextView) getView().findViewById(R.id.tv_shoplocation);
        img_mdpic_a = (ImageView) getView().findViewById(R.id.img_mdpic_a);
        img_mdpic_b = (ImageView) getView().findViewById(R.id.img_mdpic_b);
        img_mdpic_c = (ImageView) getView().findViewById(R.id.img_mdpic_c);

    }

    private void textAddXMView() {
        try {
            List<ServiceType> all_ServiceType = new DbConfig().getDbManager().selector(ServiceType.class).findAll();
            if (all_ServiceType == null) {
                Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
            } else {
                XiangmusBean xiangmusBean;

                for (int i = 0; i < all_ServiceType.size(); i++) {
                    xiangmusBean = new XiangmusBean();
                    xiangmusBean.setId(all_ServiceType.get(i).getId());
                    Log.e(TAG, "textAddXMView:  xiangmusBean.getId()  == " + xiangmusBean.getId());
                    xiangmusBean.setName(all_ServiceType.get(i).getName());
                    xiangmusBean.setColor(all_ServiceType.get(i).getColor());
                    xiangmusBean.setTime(all_ServiceType.get(i).getTime());
                    list_xms.add(xiangmusBean);
                    Log.e(TAG, "textAddXMView:   list_xms.toString()   == " + list_xms.toString());
                }
                for (int i = 0; i < (list_xms.size() % 3 == 0 ? (list_xms.size() / 3) : (list_xms.size() / 3 + 1)); i++) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.register_xm_add_item, null);
                    CheckBox checkBoxa = (CheckBox) view.findViewById(R.id.checkbox_a);
                    CheckBox checkBoxb = (CheckBox) view.findViewById(R.id.checkbox_b);
                    CheckBox checkBoxc = (CheckBox) view.findViewById(R.id.checkbox_c);
                    checkBoxa.setText(list_xms.get(i * 3 + 0).getName());
                    Log.e(TAG, "textAddXMView: check a == " + list_xms.get(i * 3 + 0).getName());
                    checkBoxa.setTag(list_xms.get(i * 3 + 0));
                    checkBoxa.setOnCheckedChangeListener(this);

                    checkBoxb.setText((i * 3 + 1 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 1).getName());
                    Log.e(TAG, "textAddXMView: check b == " + list_xms.get(i * 3 + 1).getName());
                    checkBoxb.setVisibility((i * 3 + 1 >= list_xms.size()) ? View.GONE : View.VISIBLE);
                    checkBoxb.setTag((i * 3 + 1 >= list_xms.size()) ? null : list_xms.get(i * 3 + 1));
                    checkBoxb.setOnCheckedChangeListener(this);

                    checkBoxc.setText((i * 3 + 2 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 2).getName());
                    Log.e(TAG, "textAddXMView: check c == " + list_xms.get(i * 3 + 2).getName());
                    checkBoxc.setVisibility((i * 3 + 2 >= list_xms.size()) ? View.GONE : View.VISIBLE);
                    checkBoxc.setTag((i * 3 + 2 >= list_xms.size()) ? null : list_xms.get(i * 3 + 2));
                    checkBoxc.setOnCheckedChangeListener(this);

                    ll_xiangmus.addView(view);
                }
            }

        } catch (DbException e) {
        }
    }

    private List<String> getStrLTime() {
        lTime_list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if (i < 10) {
                lTime_list.add("0" + i + ":00:00");
            } else {
                lTime_list.add(i + ":00:00");
            }
        }
        return lTime_list;
    }

    public List<String> getRTimeList(int selectedIndex) {
        rTime_list = new ArrayList<String>();
        rTime_list.addAll(lTime_list.subList(selectedIndex, lTime_list.size()));
        return rTime_list;
    }

    private void showPicDialog(String pic_url) {

        AlertDialog  dialog = new AlertDialog.Builder(getActivity()).create();
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_picture,null);
        ImageView img_dialog_pic = (ImageView) dialogView.findViewById(R.id.img_dialog_pic);
        ImageOptions myOptions = new ImageOptions.Builder()
                .setCircular(false)
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_XY)
                .setSquare(true)// setSquare和build为必须设置
                .build();
        x.image().bind(img_dialog_pic, pic_url,myOptions);
        Log.e(TAG, "showPicDialog: " +pic_url );
        dialog.setView(dialogView);
        //Glide.with(getActivity()).load(pic_url).into(img_dialog_pic);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
/*        //设置Dialog大小
        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();//为获取屏幕宽高
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();//获取对话框当前的参数值
        params.height = (int) (display.getHeight()*0.9);//设置高度未屏幕的0.8
        params.width = (int) (display.getWidth()*0.9);
        dialog.getWindow().setAttributes(params);//设置参数生效*/

    }

    private void showMyDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_logo);
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
        if (isChecked) {
            if (serviceTypeList == null || serviceTypeList.size() == 0) {
                serviceTypeList.add(bean.getId() + "");
            } else {
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (serviceTypeList.get(i).equals(bean.getId() + "")) {
                        serviceTypeList.remove(bean.getId() + "");
                    }
                }
                serviceTypeList.add(bean.getId() + "");
            }
        } else { //noChecked
            if (serviceTypeList == null || serviceTypeList.size() == 0) {

            } else {
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (serviceTypeList.get(i).equals(bean.getId() + "")) {
                        serviceTypeList.remove(bean.getId() + "");
                    }
                }
            }
        }
    }
}