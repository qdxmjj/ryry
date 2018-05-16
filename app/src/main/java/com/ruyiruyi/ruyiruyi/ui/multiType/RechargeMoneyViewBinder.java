package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class RechargeMoneyViewBinder extends ItemViewProvider<RechargeMoney, RechargeMoneyViewBinder.ViewHolder> {

    public OnMoneyClick listener;

    public void setListener(OnMoneyClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_recharge_money, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final RechargeMoney rechargeMoney) {

        if (rechargeMoney.getType() == 0) {  //0是金额   1是其他
            holder.giveMoneyText.setVisibility(View.VISIBLE);
            holder.giveMoneyText.setText("送 " + rechargeMoney.getGiveMoney() +" 元");
            holder.rechargeMoneyText.setText("冲 " +rechargeMoney.getRechargeMoney()+" 元");
        }else {
            holder.giveMoneyText.setVisibility(View.GONE);
            holder.rechargeMoneyText.setText(rechargeMoney.getRechargeMoney());
        }
        if (rechargeMoney.ischeck) { //选中
            holder.moneyLayout.setBackgroundResource(R.drawable.money_cheng_bg);
            holder.rechargeMoneyText.setTextColor(Color.WHITE);
            holder.giveMoneyText.setTextColor(0xfffffd69);
        }else {
            holder.moneyLayout.setBackgroundResource(R.drawable.money_bg);
            holder.rechargeMoneyText.setTextColor(0xff646464);
            holder.giveMoneyText.setTextColor(0xff646464);
        }
        RxViewAction.clickNoDouble(holder.moneyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onMonyeClickListener(rechargeMoney.getMoneyId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout moneyLayout;
        private final TextView rechargeMoneyText;
        private final TextView giveMoneyText;

        ViewHolder(View itemView) {
            super(itemView);
            moneyLayout = ((FrameLayout) itemView.findViewById(R.id.money_layout));
            rechargeMoneyText = ((TextView) itemView.findViewById(R.id.recharge_money_text));
            giveMoneyText = ((TextView) itemView.findViewById(R.id.give_money_text));

        }
    }

    public interface OnMoneyClick{
        void onMonyeClickListener(int moneyId);
    }
}