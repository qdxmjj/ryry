package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicBigPic;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicBigPicViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicCheckNum;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicCheckNumViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class BuyCxwyActivity extends RyBaseActivity implements PublicCheckNumViewBinder.OnPubCheckNumItemClick {

    private ActionBar actionBar;
    private ImageView img_agree;
    private TextView agreement;
    private TextView buy_;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();//Object!!!

    private int buyNum = 1; //默认买一个
    private String platNumber;
    private String cxwyPrice;
    private boolean isAgree = false; //是否勾选  默认false 未勾选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_cxwy);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("畅行无忧");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
        initData();
        setView();
        bindView();

    }

    private void bindView() {
        //选择对勾
        RxViewAction.clickNoDouble(img_agree).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!isAgree) {
                    isAgree = true;
                    img_agree.setImageResource(R.drawable.ic_yes);
                } else {
                    isAgree = false;
                    img_agree.setImageResource(R.drawable.ic_no);
                }
            }
        });
        //用户协议
        RxViewAction.clickNoDouble(agreement).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        //确认购买
        RxViewAction.clickNoDouble(buy_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!isAgree) {
                    showDialog("请同意《如驿如意畅行无忧使用协议》");
                    return;
                }
                if (buyNum < 1) {
                    showDialog("购买数量至少选择1个");
                    return;
                }
            }
        });
    }

    private void setView() {
        User user = new DbConfig().getUser();
        items.clear();
        items.add(new PublicBigPic());
        items.add(new InfoOne("用户名", user.getNick(), true));
        items.add(new InfoOne("联系电话", user.getPhone(), true));
        items.add(new InfoOne("车牌号", platNumber, true));
        items.add(new InfoOne("畅行无忧价格", cxwyPrice, true));
        items.add(new PublicCheckNum("购买数量", 999, buyNum, "1"));

        //更新适配器
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void initData() {

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rlv);
        img_agree = findViewById(R.id.img_agree);
        agreement = findViewById(R.id.agreement);
        buy_ = findViewById(R.id.buy_);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);

        register();

        mRecyclerView.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

    }

    private void register() {
        multiTypeAdapter.register(InfoOne.class, new InfoOneViewBinder());
        PublicCheckNumViewBinder publicCheckNumViewBinder = new PublicCheckNumViewBinder(getApplicationContext());
        publicCheckNumViewBinder.setListener(this);
        multiTypeAdapter.register(PublicCheckNum.class, publicCheckNumViewBinder);
        multiTypeAdapter.register(PublicBigPic.class, new PublicBigPicViewBinder());
    }

    private void showDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*
    * PublicCheckNumViewBinder    点击接口回调
    * */
    @Override
    public void onPubCheckNumItemClickListener(int num) {
        buyNum = num;
    }
}
