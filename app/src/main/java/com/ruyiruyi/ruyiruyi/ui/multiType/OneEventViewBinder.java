package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class OneEventViewBinder extends ItemViewProvider<OneEvent, OneEventViewBinder.ViewHolder> {
    private OnEventClick listener;
    private Context context;

    public OneEventViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(OnEventClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_one_event, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final OneEvent oneEvent) {
        holder.activity_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOneEventClickListener( oneEvent.getWebUrl());
            }
        });
        Glide.with(context).load(oneEvent.getImageUrl()).into(holder.activity_img);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView activity_img;

        ViewHolder(View itemView) {
            super(itemView);
            activity_img = itemView.findViewById(R.id.activity_img);
        }
    }

    public interface OnEventClick {
        void onOneEventClickListener( String webUrl);
    }
}