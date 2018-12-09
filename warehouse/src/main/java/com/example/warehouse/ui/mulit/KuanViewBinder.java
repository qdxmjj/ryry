package com.example.warehouse.ui.mulit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.warehouse.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by Lenovo on 2018/11/19.
 */
public class KuanViewBinder extends ItemViewProvider<Kuan, KuanViewBinder.ViewHolder> {
    public OnKuanClickListener listener;

    public void setListener(OnKuanClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_kuan, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Kuan kuan) {
        holder.nameView.setText(kuan.getTireKuan());
        if (kuan.isClick()){
            holder.nameLayout.setBackgroundResource(R.color.c1);
        }else {
            holder.nameLayout.setBackgroundResource(R.color.white);
        }

        RxViewAction.clickNoDouble(holder.nameLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onKuanItemClikcListener(Integer.parseInt(kuan.getTireKuan()));
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final FrameLayout nameLayout;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = ((TextView) itemView.findViewById(R.id.kuan_name_text));
            nameLayout = ((FrameLayout) itemView.findViewById(R.id.kuan_name_layout));
        }
    }
    public interface OnKuanClickListener{
        void onKuanItemClikcListener(int kuanName);
    }




}
