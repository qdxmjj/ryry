package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Lenovo on 2019/1/11.
 */
public class WuliuItemViewBinder extends ItemViewProvider<WuliuItem, WuliuItemViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_wuliu_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull WuliuItem wuliuItem) {
        String time = wuliuItem.getTime();
        String[] splited = time.split("\\s+");
        String tian = splited[0];
        String shi = splited[1];

        holder.tianView.setText(tian.substring(5,tian.length()));
        holder.shiView.setText(shi.substring(0,shi.length() - 3));
        holder.contentView.setText(wuliuItem.getWuliuStr());

        if (wuliuItem.hasShangLine) {
            holder.shangView.setVisibility(View.VISIBLE);
        }else {
            holder.shangView.setVisibility(View.GONE);
        }

        if (wuliuItem.hasXiaLan){
            holder.xiaView.setVisibility(View.VISIBLE);
        }else {
            holder.xiaView.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView shiView;
        private final TextView tianView;
        private final TextView contentView;
        private final View shangView;
        private final View xiaView;

        ViewHolder(View itemView) {
            super(itemView);
            shiView = ((TextView) itemView.findViewById(R.id.time_shi_view));
            tianView = ((TextView) itemView.findViewById(R.id.time_tian_view));
            contentView = ((TextView) itemView.findViewById(R.id.wuliu_content_view));
            shangView = ((View) itemView.findViewById(R.id.shang_xian));
            xiaView = ((View) itemView.findViewById(R.id.xia_xian));
        }
    }
}
