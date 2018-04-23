package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ShopViewBinder extends ItemViewProvider<Shop, ShopViewBinder.ViewHolder> {

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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull Shop shop) {
        final LayoutInflater inflater = LayoutInflater.from(context);

        RxViewAction.clickNoDouble(holder.shopItemLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onShopItemClickListener();
                    }
                });

        holder.mTagLayout.setAdapter(new TagAdapter<ServiceType>(shop.getServiceTypeList()) {
            @Override
            public View getView(FlowLayout parent, int position, ServiceType serviceType) {
                TextView tv = (TextView) inflater.inflate(R.layout.item_type,
                        holder.mTagLayout, false);
                tv.setTextColor(serviceType.getServiceColor());
                tv.setText(serviceType.getServiceName());
                GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                drawable.setStroke(2,serviceType.getServiceColor());

                return tv;
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TagFlowLayout mTagLayout;
        private final LinearLayout shopItemLayout;

        ViewHolder(View itemView) {
            super(itemView);
            mTagLayout = ((TagFlowLayout) itemView.findViewById(R.id.shop_tag_layout));
            shopItemLayout = (LinearLayout) itemView.findViewById(R.id.shop_item_layout);
        }
    }

    public interface OnShopItemClick{
        void onShopItemClickListener();
    }

}