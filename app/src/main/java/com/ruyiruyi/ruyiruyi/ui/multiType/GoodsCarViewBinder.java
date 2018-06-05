package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class GoodsCarViewBinder extends ItemViewProvider<GoodsCar, GoodsCarViewBinder.ViewHolder> {
    public Context context;
    public OnGoodsCarChangeClick listener;

    public GoodsCarViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(OnGoodsCarChangeClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_car, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GoodsCar goodsCar) {
        holder.goodspCountView.setText(goodsCar.getCurrentGoodsAmount()+"");
        holder.goodsNameView.setText(goodsCar.getGoodsName());
        holder.goodspPriceView.setText("￥" + goodsCar.getGoodsPrice());
        RxViewAction.clickNoDouble(holder.addView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (goodsCar.getCurrentGoodsAmount() != goodsCar.getGoodsAmount()){
                            listener.onGoodsCarChangeClickListener(goodsCar.getGoodsId(),goodsCar.getCurrentGoodsAmount() + 1,goodsCar.getGoodsClassId());
                        }else {
                            Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        RxViewAction.clickNoDouble(holder.cutView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        listener.onGoodsCarChangeClickListener(goodsCar.getGoodsId(),goodsCar.getCurrentGoodsAmount() - 1,goodsCar.getGoodsClassId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cutView;
        private final ImageView addView;
        private final TextView goodspCountView;
        private final TextView goodsNameView;
        private final TextView goodspPriceView;
        ViewHolder(View itemView) {
            super(itemView);
            cutView = ((ImageView) itemView.findViewById(R.id.cut_view));
            addView = ((ImageView) itemView.findViewById(R.id.add_view));
            goodspCountView = ((TextView) itemView.findViewById(R.id.count_view));
            goodsNameView = ((TextView) itemView.findViewById(R.id.goods_name_text));
            goodspPriceView = ((TextView) itemView.findViewById(R.id.goods_price_text));
        }
    }
    public interface OnGoodsCarChangeClick{
        void onGoodsCarChangeClickListener(int goodsId,int currentGoodsAmount,int classId);
    }
}