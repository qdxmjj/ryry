package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
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
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.text.DecimalFormat;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by Lenovo on 2018/9/30.
 */
public class HotShopViewBinder extends ItemViewProvider<HotShop, HotShopViewBinder.ViewHolder> {
    public Context context;
    public OnHotShopItemClick listence;

    public void setListence(OnHotShopItemClick listence) {
        this.listence = listence;
    }

    public HotShopViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_hot_shop, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final HotShop hotShop) {
        Glide.with(context).load(hotShop.getStoreImage()).into(holder.imageView);
        holder.nameView.setText(hotShop.getStoreName());
        double distenceDouble = Double.parseDouble(hotShop.getStoreDistence());
        if (distenceDouble > 500){
            double v = distenceDouble / 100 / 10;
            DecimalFormat df = new DecimalFormat("#.0");
            String distenceStr = df.format(v);
            holder.distenceView.setText(distenceStr + "公里");
        }else {
            holder.distenceView.setText(hotShop.getStoreDistence() + "m");
        }

        RxViewAction.clickNoDouble(holder.hotShopLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listence.onHotShopItemClickListener(hotShop.storeId,hotShop.getStoreName(),hotShop.storeImage,hotShop.getServiceTypeList(),hotShop);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView nameView;
        private final TextView distenceView;
        private final LinearLayout hotShopLayout;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.hot_iamgeview));
            nameView = ((TextView) itemView.findViewById(R.id.hot_shop_nameview));
            distenceView = ((TextView) itemView.findViewById(R.id.hot_shop_diatence));
            hotShopLayout = ((LinearLayout) itemView.findViewById(R.id.hot_shop_layout));

        }
    }

    public interface OnHotShopItemClick{
        void onHotShopItemClickListener(int storeId, String storeMame, String storeImage, List<ServiceType> storeService, HotShop shop);
    }
}
