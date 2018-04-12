package com.ruyiruyi.merchant.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;


public class WodeFragment extends Fragment {

    private ImageView img_user;
    private TextView tv_username;
    private RequestManager glideReq;
    private Boolean isLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wode_fg,container,false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

//        initDataFromDb();
    }

    private void initView() {
        img_user = (ImageView)getView().findViewById(R.id.img_user_top);
        tv_username = (TextView)getView().findViewById(R.id.tv_username);

//        DbConfig dbConfig = new DbConfig();
//        isLogin = dbConfig.getIsLogin();
    }
    private void initDataFromDb() {
        DbConfig dbConfig = new DbConfig();
        User user = dbConfig.getUser();
        String headimgurl = user.getHeadimgurl();
        String userName = user.getNick();
        glideReq = Glide.with(this);
        glideReq.load(headimgurl).transform(new GlideCircleTransform(getContext())).into(img_user);
        tv_username.setText(userName);

    }


}