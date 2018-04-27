package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ShopStrViewBinder extends ItemViewProvider<ShopStr, ShopStrViewBinder.ViewHolder> {

    public OnAllEvaluateClick listener;

    public void setListener(OnAllEvaluateClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_shop_str, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ShopStr shopStr) {
        holder.titleStr.setText(shopStr.getTitleStr());
        RxViewAction.clickNoDouble(holder.getAllEcaluate)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onAllEvaluate();
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleStr;
        private final LinearLayout getAllEcaluate;

        ViewHolder(View itemView) {
            super(itemView);
            titleStr = ((TextView) itemView.findViewById(R.id.shop_title_text));
            getAllEcaluate = ((LinearLayout) itemView.findViewById(R.id.getall_text));
        }
    }

    public interface OnAllEvaluateClick{
        void onAllEvaluate();
    }
}