package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class LoadMoreViewBinder extends ItemViewProvider<LoadMore, LoadMoreViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_load_more, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LoadMore loadMore) {
        holder.moreText.setText(loadMore.getLoadString());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView moreText;

        ViewHolder(View itemView) {
            super(itemView);
            moreText = ((TextView) itemView.findViewById(R.id.more_text));
        }
    }
}