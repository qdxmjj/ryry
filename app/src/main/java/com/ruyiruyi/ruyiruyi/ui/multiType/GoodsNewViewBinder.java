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

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class GoodsNewViewBinder extends ItemViewProvider<GoodsNew, GoodsNewViewBinder.ViewHolder> {

    private static final String TAG = GoodsNewViewBinder.class.getSimpleName();
    public Context context;
    private int currentGoodsAmount;
    public OnGoodsChangeClick listener;

    public void setListener(OnGoodsChangeClick listener) {
        this.listener = listener;
    }

    public GoodsNewViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_new, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GoodsNew goodsNew) {

        Glide.with(context).load(goodsNew.getGoodsImage()).into(holder.goodsImageView);
        holder.goodsNameView.setText(goodsNew.getGoodsName());
        holder.goodsKucuiView.setText("库存:" + goodsNew.getGoodsAmount());
        holder.goodspPriceView.setText("￥" + goodsNew.getGoodsPrice());


        holder.goodspCountView.setText(goodsNew.getCurrentGoodsAmount()+"");
        if (goodsNew.getCurrentGoodsAmount() == 0){
            holder.goodspCountView.setVisibility(View.GONE);
            holder.cutView.setVisibility(View.GONE);
        }else {
            holder.goodspCountView.setVisibility(View.VISIBLE);
            holder.cutView.setVisibility(View.VISIBLE);
        }

        RxViewAction.clickNoDouble(holder.addView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: +++" + currentGoodsAmount);

                       if (goodsNew.getCurrentGoodsAmount() != goodsNew.getGoodsAmount()){
                          listener.onGoodsChangeClickListener(goodsNew.getGoodsId(),goodsNew.getCurrentGoodsAmount() + 1,goodsNew.getGoodsClassId());
                        }else {
                           Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
                       }

                    }
                });
        RxViewAction.clickNoDouble(holder.cutView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: ---" + currentGoodsAmount);

                        listener.onGoodsChangeClickListener(goodsNew.getGoodsId(),goodsNew.getCurrentGoodsAmount() - 1,goodsNew.getGoodsClassId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView goodsImageView;
        private final ImageView cutView;
        private final ImageView addView;
        private final TextView goodsNameView;
        private final TextView goodsKucuiView;
        private final TextView goodspPriceView;
        private final TextView goodspCountView;

        ViewHolder(View itemView) {
            super(itemView);
            goodsImageView = ((ImageView) itemView.findViewById(R.id.goods_images));
            cutView = ((ImageView) itemView.findViewById(R.id.cut_view));
            addView = ((ImageView) itemView.findViewById(R.id.add_view));
            goodsNameView = ((TextView) itemView.findViewById(R.id.goods_name_text));
            goodsKucuiView = ((TextView) itemView.findViewById(R.id.goods_kucui_text));
            goodspPriceView = ((TextView) itemView.findViewById(R.id.goods_price_text));
            goodspCountView = ((TextView) itemView.findViewById(R.id.count_view));
        }
    }

    public interface OnGoodsChangeClick{
        void onGoodsChangeClickListener(int goodsId,int currentGoodsAmount,int classId);
    }
}