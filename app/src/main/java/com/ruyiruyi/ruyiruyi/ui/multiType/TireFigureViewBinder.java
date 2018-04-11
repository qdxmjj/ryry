package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class TireFigureViewBinder extends ItemViewProvider<TireFigure, TireFigureViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tire_figure, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TireFigure tireFigure) {
        if (tireFigure.isCheck) {

        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout titleLayout;
        private final TextView titleName;
        private final ImageView titleImage;
        private final LinearLayout contentLayout;
        private final ImageView bigImage;
        private final ImageView oneImage;
        private final ImageView twoImage;
        private final ImageView threeImage;
        private final TextView contentText;
        private final RecyclerView listView;

        ViewHolder(View itemView) {
            super(itemView);
            titleLayout = ((FrameLayout) itemView.findViewById(R.id.title_layout));
            titleName = ((TextView) itemView.findViewById(R.id.title_name_text));
            titleImage = ((ImageView) itemView.findViewById(R.id.title_image));
            contentLayout = ((LinearLayout) itemView.findViewById(R.id.content_layout));
            bigImage = ((ImageView) itemView.findViewById(R.id.big_image));
            oneImage = ((ImageView) itemView.findViewById(R.id.one_image));
            twoImage = ((ImageView) itemView.findViewById(R.id.two_image));
            threeImage = ((ImageView) itemView.findViewById(R.id.three_image));
            contentText = ((TextView) itemView.findViewById(R.id.content_text));
            listView = ((RecyclerView) itemView.findViewById(R.id.price_list));


        }
    }
}