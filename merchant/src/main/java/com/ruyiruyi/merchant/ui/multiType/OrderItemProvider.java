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
import com.ruyiruyi.merchant.bean.OrderItemBean;
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

public class OrderItemProvider extends ItemViewProvider<OrderItemBean, OrderItemProvider.ViewHolder> {
    private Context context;
    private String TAG = OrderItemProvider.class.getSimpleName();

    public OrderItemProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final OrderItemBean orderItemBean) {

        holder.tv_ordertitle.setText(orderItemBean.getTitle());
        holder.tv_orderbianhao.setText(orderItemBean.getBianhao());
        holder.tv_orderprice.setText(orderItemBean.getPrice());
        switch (orderItemBean.getStatus()) {
            case "0":
                holder.tv_orderstatus.setText("待评价");
                break;
            case "1":
                holder.tv_orderstatus.setText("已完成");
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button_huise);
                break;
        }
        Glide.with(context)
                .load(orderItemBean.getImgUrl())
                .into(holder.img_order);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_ordertitle;
        private final TextView tv_orderbianhao;
        private final TextView tv_orderstatus;
        private final TextView tv_orderprice;
        private final ImageView img_order;

        ViewHolder(View itemView) {
            super(itemView);
            tv_ordertitle = ((TextView) itemView.findViewById(R.id.tv_ordertitle));
            tv_orderbianhao = ((TextView) itemView.findViewById(R.id.tv_orderbianhao));
            tv_orderprice = ((TextView) itemView.findViewById(R.id.tv_orderprice));
            tv_orderstatus = ((TextView) itemView.findViewById(R.id.tv_orderstatus));
            img_order = ((ImageView) itemView.findViewById(R.id.img_order));
        }
    }

}