package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class PromotionViewBinder extends ItemViewProvider<Promotion, PromotionViewBinder.ViewHolder> {
    private Context context;
    public OnShareViewClick listener;

    public void setListener(OnShareViewClick listener) {
        this.listener = listener;
    }

    public PromotionViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.promotion_binder, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Promotion promotion) {

        holder.tv_code_promotion.setText(promotion.getPromotion());
        holder.promotion_reward.setText(promotion.getReward());
        holder.promotion_way.setText(promotion.getWay());
        RxViewAction.clickNoDouble(holder.promotion_share).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //Toast.makeText(context, "分享", Toast.LENGTH_SHORT).show();
                listener.onShareViewClikcListenner();
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_code_promotion;
        private final TextView promotion_reward;
        private final TextView promotion_way;
        private final TextView promotion_share;

        ViewHolder(View itemView) {
            super(itemView);
            tv_code_promotion = ((TextView) itemView.findViewById(R.id.tv_code_promotion));
            promotion_reward = ((TextView) itemView.findViewById(R.id.promotion_reward));
            promotion_way = ((TextView) itemView.findViewById(R.id.promotion_way));
            promotion_share = ((TextView) itemView.findViewById(R.id.promotion_share));
        }
    }

    public interface OnShareViewClick{
        void onShareViewClikcListenner();
    }

}