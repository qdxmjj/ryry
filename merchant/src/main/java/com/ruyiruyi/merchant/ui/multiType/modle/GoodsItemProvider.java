package com.ruyiruyi.merchant.ui.multiType.modle;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsBean;
import org.xutils.x;

import me.drakeet.multitype.ItemViewProvider;

public class GoodsItemProvider extends ItemViewProvider<GoodsBean, GoodsItemProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GoodsBean goodsBean) {
        holder.tv_title.setText(goodsBean.getTxt());
        holder.tv_kucun.setText(goodsBean.getKucun());
        holder.tv_yishou.setText(goodsBean.getYishou());
        holder.tv_price.setText(goodsBean.getMoney());
        x.image().bind(holder.img_goods,goodsBean.getImg_url());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_title;
        private final TextView tv_kucun;
        private final TextView tv_yishou;
        private final TextView tv_price;
        private final ImageView img_goods;

        ViewHolder(View itemView) {
            super(itemView);
            tv_title = ((TextView) itemView.findViewById(R.id.tv_goodstitle));
            tv_kucun = ((TextView) itemView.findViewById(R.id.tv_goodskucun));
            tv_yishou = ((TextView) itemView.findViewById(R.id.tv_goodsyishou));
            tv_price = ((TextView) itemView.findViewById(R.id.tv_goodsprice));
            img_goods = ((ImageView) itemView.findViewById(R.id.img_goods));
        }
    }
}