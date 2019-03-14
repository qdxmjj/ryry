package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.WXUserInfo;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Lenovo on 2019/1/14.
 */
public class WuLiuTopViewBinder extends ItemViewProvider<WuLiuTop, WuLiuTopViewBinder.ViewHolder> {

    public Context context;

    public WuLiuTopViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_wu_liu_top, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull WuLiuTop wuLiuTop) {
        Glide.with(context).load(wuLiuTop.getGoodsImage()).into(holder.goodsImageView);
        holder.goodsNameView.setText(wuLiuTop.getGoodsName());
        holder.wuliuNoView.setText(wuLiuTop.getWuliuName() + "    " + wuLiuTop.getWuliuNo());
        holder.wuliuPhoneView.setText("官方电话："  + wuLiuTop.getWuliuPhone());
        holder.address_view.setText("收货地址: " + wuLiuTop.getAddredd());
      }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView goodsImageView;
        private final TextView goodsNameView;
        private final TextView wuliuNoView;
        private final TextView wuliuPhoneView;
        private final TextView address_view;

        ViewHolder(View itemView) {
            super(itemView);
            goodsImageView = ((ImageView) itemView.findViewById(R.id.goods_image_view));
            goodsNameView = ((TextView) itemView.findViewById(R.id.goods_name_view));
            wuliuNoView = ((TextView) itemView.findViewById(R.id.wuliu_no_view));
            wuliuPhoneView = ((TextView) itemView.findViewById(R.id.wuliu_phone_view));
            address_view = ((TextView) itemView.findViewById(R.id.address_view));
        }
    }
}
