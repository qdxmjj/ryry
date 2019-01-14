package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

/**
 * Created by Lenovo on 2018/12/25.
 */
public class ExchangeCouponViewBinder extends ItemViewProvider<ExchangeCoupon, ExchangeCouponViewBinder.ViewHolder> {
    public OnCouponItemClick listener;

    public void setListener(OnCouponItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exchange_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ExchangeCoupon exchangeCoupon) {
        String[] strings = exchangeCoupon.getCouponPrice().split("\\.");
        holder.couponPriceView.setText("￥" + strings[0].toString());
        holder.couponNameView.setText(exchangeCoupon.getCouponName());
        holder.couponJifenView.setText(exchangeCoupon.getScore() + "积分");
        holder.couponKuCunView.setText("库存" + exchangeCoupon.getCouponAmount()+ "件");

        RxViewAction.clickNoDouble(holder.duihuan_coupnt_view)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCoupnItemClikcListener(exchangeCoupon.getId(),exchangeCoupon.getScore());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView couponPriceView;
        private final TextView couponNameView;
        private final TextView couponJifenView;
        private final TextView couponKuCunView;
        private final TextView duihuan_coupnt_view;

        ViewHolder(View itemView) {
            super(itemView);
            couponPriceView = ((TextView) itemView.findViewById(R.id.coupon_price_text));
            couponNameView = ((TextView) itemView.findViewById(R.id.coupon_name_view));
            couponJifenView = ((TextView) itemView.findViewById(R.id.coupon_jifen_view));
            couponKuCunView = ((TextView) itemView.findViewById(R.id.coupon_kucun_view));
            duihuan_coupnt_view = ((TextView) itemView.findViewById(R.id.duihuan_coupnt_view));
        }
    }
    public interface OnCouponItemClick{
        void onCoupnItemClikcListener(int id,String score);
    }
}
