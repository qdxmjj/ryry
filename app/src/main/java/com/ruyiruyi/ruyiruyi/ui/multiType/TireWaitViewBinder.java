package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class TireWaitViewBinder extends ItemViewProvider<TireWait, TireWaitViewBinder.ViewHolder> {
    public Context context;

    public OnWaitTireClick listener;

    public void setListener(OnWaitTireClick listener) {
        this.listener = listener;
    }

    public TireWaitViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tire_wait, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TireWait tireWait) {
        Glide.with(context).load(tireWait.getTireImage()).into(holder.tireImageView);
        holder.tireTitleText.setText(tireWait.getTireTitle());
        holder.usernameText.setText("联系人：  "+tireWait.getUsername());
        holder.tireCountText.setText("购买数量：  "+tireWait.getTireCount());
        holder.carNumberText.setText("服务对象：  "+tireWait.getCarName());
        holder.tirePlaceText.setText("位置：  "+tireWait.getTirePlace());
        holder.orderNoText.setText("订单编号：  "+tireWait.getOrderNo());
        holder.avaliabText.setText("可用数量：  " + tireWait.getAvaliableShoeNo());
       // holder.caozuoButton.setVisibility(tireWait.rejectStatus?View.VISIBLE:View.GONE);
        if (tireWait.getTireCount() != tireWait.getAvaliableShoeNo()){
            holder.caozuoButton.setVisibility(View.VISIBLE);
        }else {
            holder.caozuoButton.setVisibility(View.GONE);
        }

        RxViewAction.clickNoDouble(holder.tireLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onWaitTireClikcListener(tireWait.getOrderNo());
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView tireImageView;
        private final TextView tireTitleText;
        private final TextView usernameText;
        private final TextView tireCountText;
        private final TextView carNumberText;
        private final TextView tirePlaceText;
        private final TextView orderNoText;
        private final TextView caozuoButton;
        private final TextView avaliabText;
        private final LinearLayout tireLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tireImageView = ((ImageView) itemView.findViewById(R.id.tire_image_view));
            tireTitleText = (TextView) itemView.findViewById(R.id.tire_title_layout);
            usernameText = ((TextView) itemView.findViewById(R.id.username_text));
            tireCountText = ((TextView) itemView.findViewById(R.id.tire_count_text));
            carNumberText = ((TextView) itemView.findViewById(R.id.car_number_text));
            tirePlaceText = ((TextView) itemView.findViewById(R.id.tire_place_text));
            orderNoText = ((TextView) itemView.findViewById(R.id.order_no_text));
            caozuoButton = ((TextView) itemView.findViewById(R.id.caozuo_button));
            avaliabText = ((TextView) itemView.findViewById(R.id.tire_avaliab_count_text));
            tireLayout = ((LinearLayout) itemView.findViewById(R.id.tire_layout));

        }
    }

    public interface OnWaitTireClick{
        void onWaitTireClikcListener(String orderNo);
    }
}