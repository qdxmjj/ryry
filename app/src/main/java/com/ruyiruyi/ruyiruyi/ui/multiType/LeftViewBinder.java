package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class LeftViewBinder extends ItemViewProvider<Left, LeftViewBinder.ViewHolder> {

    public OnBigClassItemClick listener;

    public void setListener(OnBigClassItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_big_class, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Left left) {
        if (left.getClassAmount() == 0){
            holder.classCountText.setVisibility(View.GONE);
        }else {
            holder.classCountText.setVisibility(View.VISIBLE);
            holder.classCountText.setText(left.getClassAmount() + "");
        }
        holder.bigClassText.setText(left.getBigClassName());
        if (left.isCheck) {
            holder.bigClassLayout.setBackgroundResource(R.color.c1);
            holder.bigClassView.setVisibility(View.VISIBLE);
        }else {
            holder.bigClassLayout.setBackgroundResource(R.color.white);
            holder.bigClassView.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(holder.bigClassLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onLeftItemClikcListener(left.getBigClassName(),left.getClassId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout bigClassLayout;
        private final View bigClassView;
        private final TextView bigClassText;
        private final TextView classCountText;

        ViewHolder(View itemView) {
            super(itemView);
            bigClassLayout = ((FrameLayout) itemView.findViewById(R.id.big_class_layout));
            bigClassView = itemView.findViewById(R.id.big_class_view);
            bigClassText = ((TextView) itemView.findViewById(R.id.big_class_text));
            classCountText = ((TextView) itemView.findViewById(R.id.class_count_text));
        }

    }

    public interface OnBigClassItemClick{
        void onLeftItemClikcListener(String className,String classId);
    }
}