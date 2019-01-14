package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.AddAddressActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class NoAddressBeanViewBinder extends ItemViewProvider<NoAddressBean, NoAddressBeanViewBinder.ViewHolder> {
    private Context context;

    public NoAddressBeanViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_noaddress, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final NoAddressBean noAddressBean) {
        //去添加地址
        RxViewAction.clickNoDouble(holder.tv_add).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("whereIn", "listAdd");
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_add;

        ViewHolder(View itemView) {
            super(itemView);
            tv_add = ((TextView) itemView.findViewById(R.id.tv_add));
        }
    }
}