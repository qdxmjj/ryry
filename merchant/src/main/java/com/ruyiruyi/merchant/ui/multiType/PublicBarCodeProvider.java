package com.ruyiruyi.merchant.ui.multiType;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class PublicBarCodeProvider extends ItemViewProvider<PublicBarCode, PublicBarCodeProvider.ViewHolder> {
    private Context context;

    public PublicBarCodeProvider(Context context) {
        this.context = context;
    }

    private String TAG = PublicBarCodeProvider.class.getSimpleName();
    private OnBarCodeSwitchClick listener;

    public void setListener(OnBarCodeSwitchClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_public_barcode, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PublicBarCode bean) {

        holder.barcode.setText(bean.getBarCode());
        holder.barcode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {//status:   "1" 一致  "2" 不一致    默认不选 1 一致  选中 2 不一致
                    listener.OnBarCodeSwitchClickListener(bean.getBarCode(), "2", bean.getOrderNo(), bean.getId(), bean.getAbcd());
                } else {
                    listener.OnBarCodeSwitchClickListener(bean.getBarCode(), "1", bean.getOrderNo(), bean.getId(), bean.getAbcd());
                }
            }
        });
        if (bean.isShow() == null || bean.isShow().length() == 0) {
        } else {//有值 就不显示  （bean 中0 不显示   1 显示   Switch ）
            holder.barcode_switch.setVisibility(View.GONE);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView barcode;
        private final Switch barcode_switch;

        ViewHolder(View itemView) {
            super(itemView);
            barcode = ((TextView) itemView.findViewById(R.id.barcode));
            barcode_switch = ((Switch) itemView.findViewById(R.id.barcode_switch));
        }
    }

    public interface OnBarCodeSwitchClick {
        void OnBarCodeSwitchClickListener(String barcode, String status, String orderNo, String id, String abcd);
    }
}