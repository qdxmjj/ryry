package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class RoadTypeViewBinder extends ItemViewProvider<RoadType, RoadTypeViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_road_type, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RoadType roadType) {
        if (roadType.currentType == 0){
            holder.textType.setText("您经常行驶路况");
        }else if (roadType.currentType == 1){
            holder.textType.setText("您偶尔行驶路况");
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textType;

        ViewHolder(View itemView) {
            super(itemView);
            textType = ((TextView) itemView.findViewById(R.id.text_type));
        }
    }
}