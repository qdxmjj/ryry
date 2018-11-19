package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemBottomBean;

import me.drakeet.multitype.ItemViewProvider;


public class ItemBottomProvider extends ItemViewProvider<ItemBottomBean, ItemBottomProvider.ViewHolder> {
    private String TAG = ItemBottomProvider.class.getSimpleName();



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_bottom, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ItemBottomBean itemBottomBean) {
        holder.item_bottom_txt.setText(itemBottomBean.getTxt());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView item_bottom_txt;

        ViewHolder(View itemView) {
            super(itemView);
            item_bottom_txt = ((TextView) itemView.findViewById(R.id.item_bottom_txt));
        }
    }

}