package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class CouponViewBinder extends ItemViewProvider<Coupon, CouponViewBinder.ViewHolder> {

    private static final String TAG = CouponViewBinder.class.getSimpleName();
    public OnCouponClick listener;

    public void setListener(OnCouponClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Coupon coupon) {

        holder.couponNameView.setText(coupon.getCouponName());
        holder.couponBigNameView.setText(coupon.getCouponName());
        holder.couponStartTimeView.setText("使用时间： " + coupon.getStartTime() + " ~ " + coupon.getEndTime());
        List<String> storeNameList = coupon.getStoreNameList();
        if (storeNameList == null){
            holder.couponStoreView.setVisibility(View.GONE);
        }else {
            holder.couponStoreView.setVisibility(View.VISIBLE);
            String storeName = "";
            for (int i = 0; i < storeNameList.size(); i++) {
                storeName = storeName + storeNameList.get(i) + ",";
            }
            String storeNameStr = storeName.substring(0, storeName.length() - 1);
            holder.couponStoreView.setText("仅限" + storeNameStr +"使用");
        }


        if (coupon.getCouponStates() == 1){     //已使用
            holder.couponTypeView.setText("已使用");
            holder.couponColorLayout.setBackgroundResource(R.drawable.ic_huise);
        }else  if (coupon.getCouponStates() == 2){  //未使用
            holder.couponTypeView.setText("未使用");
            if (coupon.isCanUse){
                if (coupon.couponViewTypeId == 2){      //精致洗车券
                    Log.e(TAG, "onBindViewHolder: ----------" );
                    holder.couponColorLayout.setBackgroundResource(R.drawable.ic_blue);
                }else if (coupon.couponViewTypeId == 3){    //四轮定位券
                    holder.couponColorLayout.setBackgroundResource(R.drawable.ic_red);
                }else if (coupon.couponViewTypeId == 7){    //10元现金券
                    holder.couponColorLayout.setBackgroundResource(R.drawable.ic_yellow);
                }else {
                    holder.couponColorLayout.setBackgroundResource(R.drawable.ic_blue);
                }
            }else {
                Log.e(TAG, "onBindViewHolder: -++++++++-" );
                holder.couponColorLayout.setBackgroundResource(R.drawable.ic_huise);
            }
        }/*else  if (coupon.getCouponStates() == 3){  //已过期
            holder.couponTypeView.setText("已过期");
            Log.e(TAG, "onBindViewHolder: -++-----++-" );
            holder.couponColorLayout.setBackgroundResource(R.drawable.ic_huise);
        }*/else {
            holder.couponTypeView.setText("已过期");  //coupon.getCouponStates() == 3){  //已过期`
            Log.e(TAG, "onBindViewHolder: -++-----++-" );
            holder.couponColorLayout.setBackgroundResource(R.drawable.ic_huise);
        }

        if (coupon.getCouponType() == 1){       //服务券 绑定车辆
            holder.couponCarText.setVisibility(View.VISIBLE);
            holder.couponCarText.setText("仅限" + coupon.getCarNumber() + "车辆使用");
        }else if (coupon.getCouponType() == 2){ //现金券
            holder.couponCarText.setVisibility(View.GONE);
        }else {
            holder.couponCarText.setVisibility(View.GONE);
        }


        RxViewAction.clickNoDouble(holder.couponLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (coupon.isCanUse){
                            listener.onCouponClcikListener(coupon.getCouponId(),coupon.getCouponName(),coupon.getGoodsName(),coupon.couponType);
                        }

                    }
                });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout couponLayout;
        private final FrameLayout couponColorLayout;
        private final TextView couponNameView;
        private final TextView couponTypeView;
        private final TextView couponBigNameView;
        private final TextView couponCarText;
        private final TextView couponStartTimeView;
        private final TextView couponStoreView;

        ViewHolder(View itemView) {
            super(itemView);
            couponLayout = ((LinearLayout) itemView.findViewById(R.id.coupon_layout));
            couponColorLayout = ((FrameLayout) itemView.findViewById(R.id.coupon_color_layotu));
            couponNameView = ((TextView) itemView.findViewById(R.id.coupon_name_view));
            couponTypeView = ((TextView) itemView.findViewById(R.id.coupon_type_view));
            couponBigNameView = ((TextView) itemView.findViewById(R.id.coupon_big_name_view));
            couponCarText = ((TextView) itemView.findViewById(R.id.coupon_car_number_view));
            couponStartTimeView = ((TextView) itemView.findViewById(R.id.coupon_start_time_view));
            couponStoreView = ((TextView) itemView.findViewById(R.id.coupon_store_view));


        }
    }

    public interface OnCouponClick{
        void onCouponClcikListener(int couponId,String couponName,String goodsName,int couponType);
    }
}