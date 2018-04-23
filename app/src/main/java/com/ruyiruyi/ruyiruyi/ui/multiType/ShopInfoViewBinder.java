package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import me.drakeet.multitype.ItemViewProvider;

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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull ShopInfo shopInfo) {
        final LayoutInflater inflater = LayoutInflater.from(context);
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
        private final CustomBanner mBanner;

        ViewHolder(View itemView) {
            super(itemView);
            mTagLayout = ((TagFlowLayout) itemView.findViewById(R.id.shop_tag_layout));
            mBanner = (CustomBanner) itemView.findViewById(R.id.shop_info_banner);
        }
    }
}