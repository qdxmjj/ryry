package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.CircleImageView;
import com.ruyiruyi.merchant.utils.UtilsRY;

import me.drakeet.multitype.ItemViewProvider;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/30 9:18
 */
public class SaleIncomeViewBinder extends ItemViewProvider<SaleIncome, SaleIncomeViewBinder.ViewHolder> {
    private Context mContext;

    public SaleIncomeViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_saleincome, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SaleIncome saleIncome) {
//        Glide.with(mContext).load(saleIncome.getOrderImg()).into(holder.civ_orderimg);
        holder.tv_shoetitle.setText(saleIncome.getShoeTitle());
        holder.tv_shoenum.setText(saleIncome.getShoeNum() + "");
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(saleIncome.getOrderTime());
        holder.tv_ordertime.setText(timestampToStringAll);
        holder.tv_orderprice.setText(saleIncome.getOrderPrice() + "");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView civ_orderimg;
        private final TextView tv_shoetitle;
        private final TextView tv_shoenum;
        private final TextView tv_ordertime;
        private final TextView tv_orderprice;

        ViewHolder(View itemView) {
            super(itemView);
            civ_orderimg = itemView.findViewById(R.id.civ_orderimg);
            tv_shoetitle = itemView.findViewById(R.id.tv_shoetitle);
            tv_shoenum = itemView.findViewById(R.id.tv_shoenum);
            tv_ordertime = itemView.findViewById(R.id.tv_ordertime);
            tv_orderprice = itemView.findViewById(R.id.tv_orderprice);
        }
    }
}
