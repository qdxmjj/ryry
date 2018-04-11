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

public class CarVersionViewBinder extends ItemViewProvider<CarVersion, CarVersionViewBinder.ViewHolder> {

    public OnCarVersionClick listener;

    public void setListener(OnCarVersionClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_car_version, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CarVersion carVersion) {

        holder.carVersion.setText(carVersion.getCarVersion());
        RxViewAction.clickNoDouble(holder.carVersion)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCarVersionItemClickListener(carVersion.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView carVersion;

        ViewHolder(View itemView) {
            super(itemView);
            carVersion = ((TextView) itemView.findViewById(R.id.car_version));
        }
    }

    public interface OnCarVersionClick{
        void onCarVersionItemClickListener(int id);
    }
}