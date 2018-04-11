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

public class CarTitleViewBinder extends ItemViewProvider<CarTitle, CarTitleViewBinder.ViewHolder> {
    public OnCarTitlrClick listener;

    public void setListener(OnCarTitlrClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_car_title, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CarTitle carTitle) {
        holder.carTitle.setText(carTitle.getCarTitle());
        RxViewAction.clickNoDouble(holder.carTitle)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCarTitleItemClikcListener(carTitle.getCarTitle());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView carTitle;

        ViewHolder(View itemView) {
            super(itemView);
            carTitle = ((TextView) itemView.findViewById(R.id.car_title));
        }
    }

    public interface OnCarTitlrClick{
        void onCarTitleItemClikcListener(String title);
    }
}