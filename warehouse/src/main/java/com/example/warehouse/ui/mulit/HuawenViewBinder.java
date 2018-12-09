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
public class HuawenViewBinder extends ItemViewProvider<Huawen, HuawenViewBinder.ViewHolder> {
    public OnHuawenClickListener listener;

    public void setListener(OnHuawenClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_huawen, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Huawen huawen) {
        holder.nameView.setText(huawen.getHuawenName());
        if (huawen.isClick()){
            holder.huawenLayout.setBackgroundResource(R.color.c1);
        }else {
            holder.huawenLayout.setBackgroundResource(R.color.white);
        }
        RxViewAction.clickNoDouble(holder.huawenLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onHuawenItemClickListener(huawen.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final FrameLayout huawenLayout;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = ((TextView) itemView.findViewById(R.id.hua_name_text));
            huawenLayout = ((FrameLayout) itemView.findViewById(R.id.hua_name_layout));
        }
    }

    public interface OnHuawenClickListener{
        void onHuawenItemClickListener(int id);
    }
}
