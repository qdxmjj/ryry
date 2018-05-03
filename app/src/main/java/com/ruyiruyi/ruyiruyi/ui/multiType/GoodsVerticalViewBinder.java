package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.TireCountActivity;
import com.ruyiruyi.rylibrary.cell.AmountView;

import me.drakeet.multitype.ItemViewProvider;

public class GoodsVerticalViewBinder extends ItemViewProvider<GoodsVertical, GoodsVerticalViewBinder.ViewHolder> {

    public Context context;
    public int currentCount = 0;

    public GoodsVerticalViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_vertical, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GoodsVertical goodsVertical) {
        Glide.with(context).load(goodsVertical.getGoodsImage()).into(holder.goodsImageView);
        holder.goodsNameText.setText(goodsVertical.getGoodsName());
        holder.goodsPriceText.setText("￥" + goodsVertical.getGoodsPrice());


        holder.amountView.setGoods_storage(goodsVertical.getGoodsAmount());
        holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == goodsVertical.getGoodsAmount()){
                    Toast.makeText(context,"轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                currentCount =amount;

            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView goodsImageView;
        private final TextView goodsNameText;
        private final TextView goodsPriceText;
        private final AmountView amountView;

        ViewHolder(View itemView) {
            super(itemView);
            goodsImageView = ((ImageView) itemView.findViewById(R.id.goods_image));
            goodsNameText = ((TextView) itemView.findViewById(R.id.goods_name_text));
            goodsPriceText = ((TextView) itemView.findViewById(R.id.goods_price_text));
            amountView = ((AmountView) itemView.findViewById(R.id.amount_view));
        }
    }
}