package com.ruyiruyi.merchant.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.multiType.modle.Dingdan;

import me.drakeet.multitype.ItemViewProvider;

public class DingdanItemViewProvider extends ItemViewProvider<Dingdan, DingdanItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rlv_dingdan, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Dingdan dingdan) {

        switch (dingdan.getDingdan_type()) {
            case "汽车保养":
                holder.dingdan_img.setImageResource(R.drawable.ic_baoyang);
                holder.dingdan_tv_type.setText("汽车保养");
                break;
            case "美容清洗":
                holder.dingdan_img.setImageResource(R.drawable.ic_qingxi);
                holder.dingdan_tv_type.setText("美容清洗");
                break;
            case "轮胎服务":
                holder.dingdan_img.setImageResource(R.drawable.ic_luntai);
                holder.dingdan_tv_type.setText("轮胎服务");
                break;
            case "综合订单":
                holder.dingdan_img.setImageResource(R.drawable.ic_zonghe);
                holder.dingdan_tv_type.setText("综合订单");
                break;
            case "安装":
                holder.dingdan_img.setImageResource(R.drawable.ic_anzhuang);
                holder.dingdan_tv_type.setText("安装");
                break;
            case "改装":
                holder.dingdan_img.setImageResource(R.drawable.ic_gaizhuang);
                holder.dingdan_tv_type.setText("改装");
                break;
            default:
                break;
        }
        holder.dingdan_tv_carnum.setText(dingdan.getCar_num());

        // 0 空白   1 未读
        switch (dingdan.getMsg_state()) {
            case "0":
                holder.dingdan_img_state.setVisibility(View.INVISIBLE);
                break;
            case "1":
                holder.dingdan_img_state.setImageResource(R.drawable.ic_weidu);
                holder.dingdan_img_state.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView dingdan_img;
        @NonNull
        private TextView dingdan_tv_type;
        @NonNull
        private TextView dingdan_tv_carnum;
        @NonNull
        private ImageView dingdan_img_state;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dingdan_img = (ImageView) itemView.findViewById(R.id.dingdan_rlv_img);
            this.dingdan_tv_type = (TextView) itemView.findViewById(R.id.dingdan_tv_type);
            this.dingdan_tv_carnum = (TextView) itemView.findViewById(R.id.dingdan_tv_carnum);
            this.dingdan_img_state = (ImageView) itemView.findViewById(R.id.dingdan_img_state);
        }
    }
}