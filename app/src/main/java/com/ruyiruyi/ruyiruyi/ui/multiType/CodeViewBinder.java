package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class CodeViewBinder extends ItemViewProvider<Code, CodeViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_code, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Code code) {
        holder.codeText.setText(code.code);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView codeText;

        ViewHolder(View itemView) {
            super(itemView);
            codeText = ((TextView) itemView.findViewById(R.id.code_text));
        }
    }
}