package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;

public class PublicShoeFlagProvider extends ItemViewProvider<PublicShoeFlag, PublicShoeFlagProvider.ViewHolder> {
    private Context context;

    public PublicShoeFlagProvider(Context context) {
        this.context = context;
    }

    private String TAG = PublicShoeFlagProvider.class.getSimpleName();


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_public_shoeflag, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PublicShoeFlag bean) {
        Glide.with(context).load(bean.getShoeImgUrl()).into(holder.shoe_img);
        holder.shoe_name.setText(bean.getShoeName());
        holder.shoe_type.setText(bean.getOrderType());
        holder.shoe_location.setText(bean.getShoeFlag());
        holder.shoe_amount.setText(bean.getShoeAmount());
        if (bean.getHasTopline().equals("0")) {
            holder.top_line.setVisibility(View.GONE);
        } else {
            holder.top_line.setVisibility(View.VISIBLE);
        }
        if (bean.getHasBottomline().equals("0")) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView shoe_img;
        private final TextView shoe_name;
        private final TextView shoe_type;
        private final TextView shoe_location;
        private final TextView shoe_amount;
        private final View top_line;
        private final View bottom_line;

        ViewHolder(View itemView) {
            super(itemView);
            shoe_img = (ImageView) itemView.findViewById(R.id.shoe_img);
            shoe_name = ((TextView) itemView.findViewById(R.id.shoe_name));
            shoe_type = ((TextView) itemView.findViewById(R.id.shoe_type));
            shoe_location = ((TextView) itemView.findViewById(R.id.shoe_location));
            shoe_amount = ((TextView) itemView.findViewById(R.id.shoe_amount));
            top_line = ((View) itemView.findViewById(R.id.top_line));
            bottom_line = ((View) itemView.findViewById(R.id.bottom_line));
        }
    }


}