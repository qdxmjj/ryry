package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.AddAddressActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class MyAddressViewBinder extends ItemViewProvider<MyAddress, MyAddressViewBinder.ViewHolder> {
    private Context context;
    private ForRefreshAddress listener;

    public MyAddressViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(ForRefreshAddress listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_myaddress, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyAddress myAddress) {
        holder.tv_name.setText(myAddress.getName());
        holder.tv_phone.setText(myAddress.getPhone());
        holder.tv_address.setText("收货地址: " + myAddress.getAddress());
        int isDefault = myAddress.getIsDefault();
        holder.tv_default.setVisibility(isDefault == 1 ? View.VISIBLE : View.GONE);
        holder.iv_default.setVisibility(isDefault == 1 ? View.VISIBLE : View.GONE);
        //删除操作
        RxViewAction.clickNoDouble(holder.tv_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //接口回调
                listener.gotoRefresh(myAddress.getId());
            }
        });
        //编辑操作
        RxViewAction.clickNoDouble(holder.tv_reedit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //确认
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("whereIn", "reedit");
                Bundle extras = new Bundle();
                extras.putInt("id", myAddress.getId());
                extras.putString("name", myAddress.getName());
                extras.putString("phone", myAddress.getPhone());
                extras.putString("sheng", myAddress.getSheng());
                extras.putString("shi", myAddress.getShi());
                extras.putString("qu", myAddress.getQu());
                extras.putString("address", myAddress.getAddress());
                extras.putInt("isDefault", myAddress.getIsDefault());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
        //条目选择
        RxViewAction.clickNoDouble(holder.ll_item).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.gotoChoose(myAddress);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final TextView tv_phone;
        private final TextView tv_address;
        private final TextView tv_delete;
        private final TextView tv_reedit;
        private final ImageView iv_default;
        private final TextView tv_default;
        private final LinearLayout ll_item;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_phone = ((TextView) itemView.findViewById(R.id.tv_phone));
            tv_address = ((TextView) itemView.findViewById(R.id.tv_address));
            tv_delete = ((TextView) itemView.findViewById(R.id.tv_delete));
            tv_reedit = ((TextView) itemView.findViewById(R.id.tv_reedit));
            iv_default = (ImageView) itemView.findViewById(R.id.iv_default);
            tv_default = (TextView) itemView.findViewById(R.id.tv_default);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }
    }


    public interface ForRefreshAddress {
        void gotoRefresh(int id);

        void gotoChoose(MyAddress myAddress);
    }


}