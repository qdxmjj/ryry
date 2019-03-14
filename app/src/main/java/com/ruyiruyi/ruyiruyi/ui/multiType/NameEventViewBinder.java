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


/**
 * Created by Lenovo on 2019/1/24.
 */
public class NameEventViewBinder extends ItemViewProvider<NameEvent, NameEventViewBinder.ViewHolder> {
    private Context context;

    public NameEventViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_name_event, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NameEvent nameEvent) {
        Glide.with(context).load(nameEvent.getEvent().getImageUrl()).into(holder.imageView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.activity_img));
        }
    }
}
