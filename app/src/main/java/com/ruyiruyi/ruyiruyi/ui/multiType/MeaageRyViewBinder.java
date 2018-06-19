package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class MeaageRyViewBinder extends ItemViewProvider<MeaageRy, MeaageRyViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_meaage_ry, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MeaageRy meaageRy) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderTypeView;
        private final ImageView orderImageView;
        private final TextView orderNameView;
        private final TextView orderTimeView;
        private final TextView orderNoView;

        ViewHolder(View itemView) {
            super(itemView);
            orderTypeView = (TextView) itemView.findViewById(R.id.order_type_view);
            orderImageView = ((ImageView) itemView.findViewById(R.id.order_image_view));
            orderNameView = ((TextView) itemView.findViewById(R.id.order_name_view));
            orderTimeView = ((TextView) itemView.findViewById(R.id.order_time_view));
            orderNoView = ((TextView) itemView.findViewById(R.id.order_no_view));
        }
    }
}