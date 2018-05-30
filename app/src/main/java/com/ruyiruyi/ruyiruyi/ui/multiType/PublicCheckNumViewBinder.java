package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.cell.AmountView;

import me.drakeet.multitype.ItemViewProvider;

public class PublicCheckNumViewBinder extends ItemViewProvider<PublicCheckNum, PublicCheckNumViewBinder.ViewHolder> {

    private static final String TAG = PublicCheckNumViewBinder.class.getSimpleName();
    public Context context;
    public int currentCount = 0;
    public OnPubCheckNumItemClick listener;

    public void setListener(OnPubCheckNumItemClick listener) {
        this.listener = listener;
    }

    public PublicCheckNumViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_public_checknum, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PublicCheckNum bean) {
        holder.title.setText(bean.getTitle());

        holder.amountView.setGoods_storage(bean.getStorage());
        holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == bean.getStorage()) {
                    Toast.makeText(context, "已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                currentCount = amount;
                bean.setCurrent_num(amount);
                Log.e(TAG, "onAmountChange: xxx");
                listener.onPubCheckNumItemClickListener(bean.getCurrent_num());
            }
        });
        holder.amountView.setAmount(bean.getCurrent_num());

        if (bean.getHasBottomline().equals("1")) {
            holder.bottom_line.setVisibility(View.VISIBLE);
        } else {
            holder.bottom_line.setVisibility(View.INVISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final AmountView amountView;
        private final View bottom_line;

        ViewHolder(View itemView) {
            super(itemView);
            title = ((TextView) itemView.findViewById(R.id.title));
            amountView = ((AmountView) itemView.findViewById(R.id.amount));
            bottom_line = ((View) itemView.findViewById(R.id.bottom_line));
        }
    }

    public interface OnPubCheckNumItemClick {
        void onPubCheckNumItemClickListener(int num);
    }
}