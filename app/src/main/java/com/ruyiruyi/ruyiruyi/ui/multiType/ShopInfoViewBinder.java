package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.RouteMapActivity;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ShopInfoViewBinder extends ItemViewProvider<ShopInfo, ShopInfoViewBinder.ViewHolder> {
    public Context context;

    public ShopInfoViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_shop_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final ShopInfo shopInfo) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        holder.storeNameText.setText(shopInfo.getStoreName());
        holder.storeTypeText.setText(shopInfo.getStoreTypeName());
        int typeColor = Color.parseColor(shopInfo.getStoreTypeColor());
        GradientDrawable drawable = (GradientDrawable) holder.storeTypeText.getBackground();
        drawable.setStroke(2,typeColor);
        drawable.setColor(typeColor );
        holder.storeAddressText.setText("地址："  + shopInfo.getStoreAddress());
        holder.storeContentText.setText(shopInfo.getStoreDescribe());
        holder.storePhoneText.setText("联系方式: " + shopInfo.getStorePhone());
        holder.storeDistence.setText(shopInfo.getStoreDistence() + "m");

        //RouteMap 路线 Activity
        RxViewAction.clickNoDouble(holder.ll_routemap).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(context, RouteMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("storeName",shopInfo.getStoreName());
                bundle.putString("store_latitude",shopInfo.getStore_latitude());
                bundle.putString("store_longitude",shopInfo.getStore_longitude());
                bundle.putString("user_latitude",shopInfo.getUser_latitude());
                bundle.putString("user_longitude",shopInfo.getUser_longitude());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        holder.mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        },shopInfo.getImageList())
                .startTurning(5000);


        holder.mTagLayout.setAdapter(new TagAdapter<ServiceType>(shopInfo.getServiceTypeList()) {
            @Override
            public View getView(FlowLayout parent, int position, ServiceType serviceType) {
                TextView tv = (TextView) inflater.inflate(R.layout.item_type,
                        holder.mTagLayout, false);
                int parseColor = Color.parseColor(serviceType.getServiceColor());
                tv.setTextColor(parseColor);
                tv.setText(serviceType.getServiceName());
                GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                drawable.setStroke(2,parseColor);

                return tv;
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TagFlowLayout mTagLayout;
        private final CustomBanner mBanner;
        private final TextView storeNameText;
        private final TextView storeTypeText;
        private final TextView storeAddressText;
        private final TextView storePhoneText;
        private final TextView storeContentText;
        private final TextView storeDistence;
        private final LinearLayout ll_routemap;

        ViewHolder(View itemView) {
            super(itemView);
            mTagLayout = ((TagFlowLayout) itemView.findViewById(R.id.shop_tag_layout));
            mBanner = (CustomBanner) itemView.findViewById(R.id.shop_info_banner);
            storeNameText = (TextView) itemView.findViewById(R.id.store_name_text);
            storeTypeText = (TextView) itemView.findViewById(R.id.store_type_name);
            storeAddressText = (TextView) itemView.findViewById(R.id.store_addresee_text);
            storePhoneText = (TextView) itemView.findViewById(R.id.store_phone_text);
            storeContentText = (TextView) itemView.findViewById(R.id.store_content_text);
            storeDistence = (TextView) itemView.findViewById(R.id.store_distence_text);
            ll_routemap = (LinearLayout) itemView.findViewById(R.id.ll_routemap);
        }
    }
}