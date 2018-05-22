package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class TireInfoViewBinder extends ItemViewProvider<TireInfo, TireInfoViewBinder.ViewHolder> {
    public Context context;

    public TireInfoViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tire_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TireInfo tireInfo) {
        Glide.with(context).load(tireInfo.getTireImage()).into(holder.tireImage);
        holder.tireName.setText(tireInfo.getTireName());
        if (tireInfo.getFontRearFlag().equals("0")){  //0:前后一致 1:前轮 2:后轮
            holder.tirePlace.setText("位置： 前轮/后轮");
        }else if (tireInfo.getFontRearFlag().equals("1")){
            holder.tirePlace.setText("位置： 前轮");
        }else if (tireInfo.getFontRearFlag().equals("2")){
            holder.tirePlace.setText("位置： 后轮");
        }
        if (tireInfo.getTirePrice().equals("0.00")){
            holder.tirePrice.setVisibility(View.GONE);
        } else {
            holder.tirePrice.setVisibility(View.VISIBLE);
        }
        holder.tirePrice.setText("￥"+ tireInfo.getTirePrice());
        holder.tireCount.setText("×" + tireInfo.getTireCount());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView tireImage;
        private final TextView tireName;
        private final TextView tirePrice;
        private final TextView tireCount;
        private final TextView tirePlace;
        ViewHolder(View itemView) {
            super(itemView);
            tireImage = ((ImageView) itemView.findViewById(R.id.tire_image));
            tireName = ((TextView) itemView.findViewById(R.id.tire_name_text_oder));
            tirePrice = ((TextView) itemView.findViewById(R.id.tire_price_text_order));
            tireCount = ((TextView) itemView.findViewById(R.id.tire_count_order));
            tirePlace = ((TextView) itemView.findViewById(R.id.tire_place_text));
        }
    }
}