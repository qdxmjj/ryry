package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class ShoppingPointsInfoViewBinder extends ItemViewProvider<ShoppingPointsInfo, ShoppingPointsInfoViewBinder.ViewHolder> {
    private Context mContext;

    public ShoppingPointsInfoViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_shoppingpoints_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ShoppingPointsInfo shoppingPointsInfo) {
        holder.tv_title.setText(shoppingPointsInfo.getTitle());
        int type = shoppingPointsInfo.getIncomeType();//(0:收入,1:支出)
        if (type == 1) {
            holder.tv_type.setText("-");
            holder.tv_type.setTextColor(mContext.getResources().getColor(R.color.c6));
            holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.c6));
        }else {
            holder.tv_type.setText("+");
            holder.tv_type.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
            holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
        }
        int points = shoppingPointsInfo.getPoints();
        holder.tv_count.setText(points + "");
        holder.tv_time.setText(shoppingPointsInfo.getTimeStr());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_title;
        private final TextView tv_time;
        private final TextView tv_count;
        private final TextView tv_type;

        ViewHolder(View itemView) {
            super(itemView);
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
            tv_count = ((TextView) itemView.findViewById(R.id.tv_count));
            tv_type = ((TextView) itemView.findViewById(R.id.tv_type));
        }
    }

}