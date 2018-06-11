package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.cell.AmountView;

import java.util.Collections;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;

public class CountOneViewBinder extends ItemViewProvider<CountOne, CountOneViewBinder.ViewHolder> {

    public Context context;
    public int currentCount = 0;
    public List<Double> priceList;
    public OnCxwyCountClikc listener;
    public double allPrice = 0.00;

    public void setListener(OnCxwyCountClikc listener) {
        this.listener = listener;
    }

    public CountOneViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_count_one, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final CountOne countOne) {
        priceList = countOne.getPriceList();
        Collections.sort(priceList);
        for (int i = 0; i < priceList.size(); i++) {
            allPrice  = allPrice + priceList.get(i);
        }
        holder.buchaPriceText.setText("￥"+ allPrice);
        holder.amountView.setGoods_storage(countOne.getCxwyCount());
        holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == countOne.getCxwyCount()){
                    Toast.makeText(context,"已达到最大数", Toast.LENGTH_SHORT).show();
                }
                currentCount =amount;
                allPrice = 0.00;
                for (int i = 0; i < priceList.size() - currentCount; i++) {
                    allPrice = allPrice + priceList.get(i);
                }
                holder.buchaPriceText.setText("￥"+ allPrice);
                listener.onCxwyCountClikcListener(currentCount,allPrice);
            }
        });
        holder.amountView.setAmount(countOne.getCurrentCxwyCount());
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AmountView amountView;
        private final TextView buchaPriceText;

        ViewHolder(View itemView) {
            super(itemView);
            amountView = ((AmountView) itemView.findViewById(R.id.cxwy_amount_view));
            buchaPriceText = ((TextView) itemView.findViewById(R.id.bucha_price_text));
        }
    }

    public interface OnCxwyCountClikc{
        void onCxwyCountClikcListener(int cxwyCount,Double currenPrice);
    }
}