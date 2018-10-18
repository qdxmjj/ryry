package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.CircleImageView;

import me.drakeet.multitype.ItemViewProvider;

public class PublicGoodsInfoProvider extends ItemViewProvider<PublicGoodsInfo, PublicGoodsInfoProvider.ViewHolder> {
    private Context context;

    public PublicGoodsInfoProvider(Context context) {
        this.context = context;
    }

    private String TAG = PublicGoodsInfoProvider.class.getSimpleName();


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_public_goods_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PublicGoodsInfo bean) {

        Glide.with(context).load(bean.getDetailImage()).into(holder.goods_img);
        holder.goods_name.setText(bean.getDetailName());
        holder.goods_price.setText(bean.getDetailPrice());
        holder.goods_amount.setText("x " + bean.getAmount());


        if (bean.getHasTopline().equals("0")) {
            holder.top_line.setVisibility(View.GONE);
        } else {
            holder.top_line.setVisibility(View.VISIBLE);
        }
        if (bean.getHasBottomline().equals("0")) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView goods_img;
        private final TextView goods_name;
        private final TextView goods_price;
        private final TextView goods_amount;
        private final View top_line;
        private final View bottom_line;

        ViewHolder(View itemView) {
            super(itemView);
            goods_img = (CircleImageView) itemView.findViewById(R.id.goods_img);
            goods_name = ((TextView) itemView.findViewById(R.id.goods_name));
            goods_price = ((TextView) itemView.findViewById(R.id.goods_price));
            goods_amount = ((TextView) itemView.findViewById(R.id.goods_amount));
            top_line = (View) itemView.findViewById(R.id.top_line);
            bottom_line = (View) itemView.findViewById(R.id.bottom_line);
        }
    }

}