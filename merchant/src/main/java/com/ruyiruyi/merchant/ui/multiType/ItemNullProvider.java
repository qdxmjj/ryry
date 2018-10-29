package com.ruyiruyi.merchant.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ItemNullBean;

import me.drakeet.multitype.ItemViewProvider;

public class ItemNullProvider extends ItemViewProvider<ItemNullBean, ItemNullProvider.ViewHolder> {
    private String TAG = ItemNullProvider.class.getSimpleName();


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_null, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ItemNullBean itemNullBean) {
        if (itemNullBean.getResPicId() != 100100) {
            holder.img_null.setImageResource(itemNullBean.getResPicId());
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_null;

        ViewHolder(View itemView) {
            super(itemView);
            img_null = ((ImageView) itemView.findViewById(R.id.item_null_img));
        }
    }

}