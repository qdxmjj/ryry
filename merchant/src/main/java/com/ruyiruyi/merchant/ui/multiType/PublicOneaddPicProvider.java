package com.ruyiruyi.merchant.ui.multiType;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class PublicOneaddPicProvider extends ItemViewProvider<PublicOneaddPic, PublicOneaddPicProvider.ViewHolder> {
    private Context context;

    public PublicOneaddPicProvider(Context context) {
        this.context = context;
    }

    private String TAG = PublicOneaddPicProvider.class.getSimpleName();


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_public_oneaddpic, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PublicOneaddPic bean) {

        holder.title.setText(bean.getTitle());
        holder.content.setText(bean.getContent());

        if (bean.isHasPic()) {//判断是否加右边图片
            holder.right_pic.setVisibility(View.VISIBLE);
        } else {
            holder.right_pic.setVisibility(View.GONE);
        }

        if (bean.isHasBottom()) {//判断是否底部加线
            holder.bottom_line.setVisibility(View.VISIBLE);
        } else {
            holder.bottom_line.setVisibility(View.GONE);
        }

        //右边电话图片监听
        RxViewAction.clickNoDouble(holder.right_pic).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //再判断用户是否给与电话权限
                if (judgeIsPower()) {
                    //用户已给权限拨打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + bean.getContent());
                    intent.setData(data);
                    context.startActivity(intent);
                }
            }
        });
    }

    private boolean judgeIsPower() {
        Boolean isPOW;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "请授权电话权限", Toast.LENGTH_SHORT).show();
            isPOW = false;
        } else {
            isPOW = true;
        }

        return isPOW;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView content;
        private final ImageView right_pic;
        private final View bottom_line;

        ViewHolder(View itemView) {
            super(itemView);
            title = ((TextView) itemView.findViewById(R.id.title));
            content = ((TextView) itemView.findViewById(R.id.content));
            right_pic = (ImageView) itemView.findViewById(R.id.right_pic);
            bottom_line = (View) itemView.findViewById(R.id.bottom_line);
        }
    }

}