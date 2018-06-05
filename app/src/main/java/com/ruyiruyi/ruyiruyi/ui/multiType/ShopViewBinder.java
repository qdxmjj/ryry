package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;

import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ShopViewBinder extends ItemViewProvider<Shop, ShopViewBinder.ViewHolder> {

    private static final String TAG = ShopViewBinder.class.getSimpleName();
    public Context context;
    public OnShopItemClick listener;

    public void setListener(OnShopItemClick listener) {
        this.listener = listener;
    }
    public ShopViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Shop shop) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        Glide.with(context).load(shop.getStoreImage()).into(holder.shopImage);
        holder.shopNameText.setText(shop.getStoreName());
        holder.addressText.setText("地址： " + shop.getStoreAddress());
        if (shop.getStoreDistence().equals("")){
            holder.distenceText.setText(shop.getStoreDistence());
        }else {
            holder.distenceText.setText(shop.getStoreDistence() + "km");
        }

        holder.shopTypeText.setText(shop.getStoreTypeName());
        int typeColor = Color.parseColor(shop.getStoreTypreColoe());
        GradientDrawable drawable = (GradientDrawable) holder.shopTypeText.getBackground();
        drawable.setStroke(2,typeColor);
        drawable.setColor(typeColor );


        RxViewAction.clickNoDouble(holder.shopItemLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onShopItemClickListener(shop.getStoreId(),shop.getStoreName(),shop.getStoreImage(),shop.getServiceTypeList(),shop);
                    }
                });

        Log.e(TAG, "onBindViewHolder: " + shop.getServiceTypeList().size());
        holder.mTagLayout.setAdapter(new TagAdapter<ServiceType>(shop.getServiceTypeList()) {
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
        private final LinearLayout shopItemLayout;
        private final ImageView shopImage;
        private final TextView shopNameText;
        private final TextView addressText;
        private final TextView distenceText;
        private final TextView shopTypeText;

        ViewHolder(View itemView) {
            super(itemView);
            mTagLayout = ((TagFlowLayout) itemView.findViewById(R.id.shop_tag_layout));
            shopItemLayout = (LinearLayout) itemView.findViewById(R.id.shop_item_layout);
            shopImage = ((ImageView) itemView.findViewById(R.id.shop_image));
            shopNameText = ((TextView) itemView.findViewById(R.id.shop_name_text));
            addressText = ((TextView) itemView.findViewById(R.id.shop_address_text));
            distenceText = ((TextView) itemView.findViewById(R.id.shop_distence_text));
            shopTypeText = ((TextView) itemView.findViewById(R.id.shop_type_text));


        }
    }

    public interface OnShopItemClick{
        void onShopItemClickListener(int storeId, String storeMame, String storeImage, List<ServiceType> storeService, Shop shop);
    }

}