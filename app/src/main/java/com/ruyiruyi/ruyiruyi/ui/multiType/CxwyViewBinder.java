package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class CxwyViewBinder extends ItemViewProvider<Cxwy, CxwyViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_cxwy, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Cxwy cxwy) {
        if (cxwy.getCxwyType() == 2) {  //1是赠送  2是购买
            holder.cxwyLayout.setBackgroundResource(R.drawable.cxwy_normal);
            holder.cxwyTimeText.setText("限制使用时间： 无限制" );
        }else if (cxwy.getCxwyType() == 1){
            holder.cxwyLayout.setBackgroundResource(R.drawable.cxwy_give);
            holder.cxwyTimeText.setText("限制使用时间： " + cxwy.getCxwyStartTime() + "-" + cxwy.getCxwyEndTime());
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout cxwyLayout;
        private final TextView cxwyTimeText;

        ViewHolder(View itemView) {
            super(itemView);
            cxwyLayout = ((FrameLayout) itemView.findViewById(R.id.cxwy_layout));
            cxwyTimeText = ((TextView) itemView.findViewById(R.id.cxwy_time_text));
        }
    }
}