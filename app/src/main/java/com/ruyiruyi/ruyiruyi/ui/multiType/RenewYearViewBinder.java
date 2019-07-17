package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by 86135 on 2019/5/28.
 */
public class RenewYearViewBinder extends ItemViewProvider<RenewYear, RenewYearViewBinder.ViewHolder> {

    public OnRenewChoosListener listener;
    public Context context;

    public RenewYearViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(OnRenewChoosListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_renew_year, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final RenewYear renewYear) {
        holder.priceTextView.setText(renewYear.getPrice() + "元");
        holder.yearTextView.setText(renewYear.getYear()+"年");
        if (renewYear.isChoose){
            holder.renewLayout.setBackgroundResource(R.drawable.bg_button_cheng);
            holder.priceTextView.setTextColor(context.getResources().getColor(R.color.c21));
            holder.yearTextView.setTextColor(context.getResources().getColor(R.color.c21));
        }else {
            holder.renewLayout.setBackgroundResource(R.drawable.bg_button_while);
            holder.priceTextView.setTextColor(context.getResources().getColor(R.color.c6));
            holder.yearTextView.setTextColor(context.getResources().getColor(R.color.c6));
        }
        RxViewAction.clickNoDouble(holder.renewLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onReneItemClickListener(renewYear.getYear());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView priceTextView;
        private final TextView yearTextView;
        private final LinearLayout renewLayout;

        ViewHolder(View itemView) {
            super(itemView);
            priceTextView = ((TextView) itemView.findViewById(R.id.price_text));
            yearTextView = ((TextView) itemView.findViewById(R.id.year_text));
            renewLayout = ((LinearLayout) itemView.findViewById(R.id.renew_layout));
        }
    }

    public interface OnRenewChoosListener{
        void onReneItemClickListener(int year);
    }
}
