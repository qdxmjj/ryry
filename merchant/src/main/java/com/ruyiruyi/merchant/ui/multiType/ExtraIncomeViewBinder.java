package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.utils.UtilsRY;

import me.drakeet.multitype.ItemViewProvider;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/30 16:58
 */
public class ExtraIncomeViewBinder extends ItemViewProvider<ExtraIncome, ExtraIncomeViewBinder.ViewHolder> {
    private Context mContext;

    public ExtraIncomeViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_extraincome, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ExtraIncome extraIncome) {
        holder.tv_orderprice.setText(extraIncome.getOrderPrice() + "");
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(extraIncome.getOrderTime());
        holder.tv_ordertime.setText(timestampToStringAll);
        String jsoPhone = extraIncome.getInvitePhone();
        String invitePhone = jsoPhone.substring(0, 3) + "****" + jsoPhone.substring(jsoPhone.length() - 4);
        holder.tv_invitephone.setText(invitePhone);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_orderprice;
        private final TextView tv_ordertime;
        private final TextView tv_invitephone;

        ViewHolder(View itemView) {
            super(itemView);
            tv_orderprice = itemView.findViewById(R.id.tv_orderprice);
            tv_ordertime = itemView.findViewById(R.id.tv_ordertime);
            tv_invitephone = itemView.findViewById(R.id.tv_invitephone);
        }
    }
}
