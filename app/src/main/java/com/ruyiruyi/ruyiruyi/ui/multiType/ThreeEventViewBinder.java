package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ThreeEventViewBinder extends ItemViewProvider<ThreeEvent, ThreeEventViewBinder.ViewHolder> {
    public OnEventItemClickListener listener;

    public void setListener(OnEventItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_three_event, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ThreeEvent threeEvent) {
        RxViewAction.clickNoDouble(holder.cxwyImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onEventClickListener("cxwy");
                    }
                });
        RxViewAction.clickNoDouble(holder.qcbyImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onEventClickListener("qcby");
                    }
                });
        RxViewAction.clickNoDouble(holder.mrqxImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onEventClickListener("mrqx");
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cxwyImage;
        private final ImageView qcbyImage;
        private final ImageView mrqxImage;

        ViewHolder(View itemView) {
            super(itemView);
            cxwyImage = ((ImageView) itemView.findViewById(R.id.cxwy_image));
            qcbyImage = ((ImageView) itemView.findViewById(R.id.qcby_image));
            mrqxImage = ((ImageView) itemView.findViewById(R.id.mrqx_image));
        }
    }

    public interface OnEventItemClickListener{
        void onEventClickListener(String tag);
    }
}