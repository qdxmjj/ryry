package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class CreditLimitViewBinder extends ItemViewProvider<CreditLimit, CreditLimitViewBinder.ViewHolder> {
    public Context context;

    public CreditLimitViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_credit_limit, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CreditLimit creditLimit) {
        Glide.with(context).load(creditLimit.getCarImage()).into(holder.carImage);
        holder.carNameText.setText(creditLimit.getCarName());
        holder.carNumberText.setText("车牌号 ：" + creditLimit.getCarNumber());
        holder.creditLimitText.setText(creditLimit.getCreditLimit());
        holder.creditLimitRemainText.setText(creditLimit.getCreditLimitRemain());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView carImage;
        private final TextView carNameText;
        private final TextView carNumberText;
        private final TextView creditLimitText;
        private final TextView creditLimitRemainText;

        ViewHolder(View itemView) {
            super(itemView);
            carImage = ((ImageView) itemView.findViewById(R.id.car_image_limit));
            carNameText = ((TextView) itemView.findViewById(R.id.car_name_limit));
            carNumberText = ((TextView) itemView.findViewById(R.id.car_number_limit));
            creditLimitText = ((TextView) itemView.findViewById(R.id.credit_limit_text));
            creditLimitRemainText = ((TextView) itemView.findViewById(R.id.credit_limit_remain_text));

        }
    }
}