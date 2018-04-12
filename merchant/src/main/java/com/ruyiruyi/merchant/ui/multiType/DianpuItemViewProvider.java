package com.ruyiruyi.merchant.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.multiType.modle.Dianpu;

import me.drakeet.multitype.ItemViewProvider;

public class DianpuItemViewProvider extends ItemViewProvider<Dianpu, DianpuItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rlv_dianpu, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Dianpu dianpu) {

        switch (dianpu.getDianpu_type()) {
            case "汽车保养":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_baoyang);
                holder.dianpu_tv_type.setText("汽车保养");
                break;
            case "美容清洗":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_qingxi);
                holder.dianpu_tv_type.setText("美容清洗");
                break;
            case "轮胎服务":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_luntai);
                holder.dianpu_tv_type.setText("轮胎服务");
                break;
            case "综合订单":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_zonghe);
                holder.dianpu_tv_type.setText("综合订单");
                break;
            case "安装":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_anzhuang);
                holder.dianpu_tv_type.setText("安装");
                break;
            case "改装":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_gaizhuang);
                holder.dianpu_tv_type.setText("改装");
                break;
            default:
                break;
        }
        holder.dianpu_tv_time.setText(dianpu.getDianpu_time());
        holder.dianpu_tv_money.setText(dianpu.getDianpu_money());

        // 0 未完成   1 已完成
        switch (dianpu.getDianpu_state()) {
            case "0":

                break;
            case "1":

                break;
            default:
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView dianpu_rlv_img;
        @NonNull
        private TextView dianpu_tv_type;
        @NonNull
        private TextView dianpu_tv_time;
        @NonNull
        private TextView dianpu_tv_money;
        @NonNull
        private TextView dianpu_tv_state;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dianpu_rlv_img = (ImageView) itemView.findViewById(R.id.dianpu_rlv_img);
            this.dianpu_tv_type = (TextView) itemView.findViewById(R.id.dianpu_tv_type);
            this.dianpu_tv_time = (TextView) itemView.findViewById(R.id.dianpu_tv_time);
            this.dianpu_tv_money = (TextView) itemView.findViewById(R.id.dianpu_tv_money);
            this.dianpu_tv_state = (TextView) itemView.findViewById(R.id.dianpu_tv_state);
        }
    }
}