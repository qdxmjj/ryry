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
public class BianViewBinder extends ItemViewProvider<Bian, BianViewBinder.ViewHolder> {

    public OnBianClikcListener listener;

    public void setListener(OnBianClikcListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_bian, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Bian bian) {
        holder.nameText.setText(bian.getBianNmae());
        if (bian.isClick()){
            holder.nameLayout.setBackgroundResource(R.color.c1);
        }else {
            holder.nameLayout.setBackgroundResource(R.color.white);
        }
        RxViewAction.clickNoDouble(holder.nameLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onBianItemClickListener(Integer.parseInt(bian.getBianNmae()));
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameText;
        private final FrameLayout nameLayout;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = ((TextView) itemView.findViewById(R.id.name_text));
            nameLayout = ((FrameLayout) itemView.findViewById(R.id.name_bian_layout));
        }
    }

    public interface OnBianClikcListener{
        void onBianItemClickListener(int bianName);
    }
}
