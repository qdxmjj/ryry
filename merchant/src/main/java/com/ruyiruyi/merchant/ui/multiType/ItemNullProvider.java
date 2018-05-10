package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsItemBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.ui.activity.GoodsInfoReeditActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ItemNullProvider extends ItemViewProvider<ItemNullBean, ItemNullProvider.ViewHolder> {
    private String TAG = ItemNullProvider.class.getSimpleName();



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_null, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ItemNullBean itemNullBean) {
//        holder.img_null.setImageResource();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_null;

        ViewHolder(View itemView) {
            super(itemView);
            img_null = ((ImageView) itemView.findViewById(R.id.item_null_img));
        }
    }

}