package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class FunctionViewBinder extends ItemViewProvider<Function, FunctionViewBinder.ViewHolder> {

    public  OnFunctionItemClick listener;

    public void setListener(OnFunctionItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_function, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Function function) {

        RxViewAction.clickNoDouble(holder.tireBuyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onFunctionClickListener(0);
                    }
                });
        RxViewAction.clickNoDouble(holder.tireChangeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onFunctionClickListener(1);
                    }
                });
        RxViewAction.clickNoDouble(holder.tireXiubuLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onFunctionClickListener(2);
                    }
                });
        RxViewAction.clickNoDouble(holder.tireShreLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onFunctionClickListener(3);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout tireBuyLayout;
        private final LinearLayout tireChangeLayout;
        private final LinearLayout tireXiubuLayout;
        private final LinearLayout tireShreLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tireBuyLayout = ((LinearLayout) itemView.findViewById(R.id.tire_buy));
            tireChangeLayout = ((LinearLayout) itemView.findViewById(R.id.tire_change));
            tireXiubuLayout = ((LinearLayout) itemView.findViewById(R.id.tire_xiubu));
            tireShreLayout = ((LinearLayout) itemView.findViewById(R.id.tire_share));
        }
    }

    public interface OnFunctionItemClick{
        void onFunctionClickListener(int type);
    }
}