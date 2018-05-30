package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class GoodsClassViewBinder extends ItemViewProvider<GoodsClass, GoodsClassViewBinder.ViewHolder> {

    public Context context;

    public GoodsClassViewBinder(Context context) {
        this.context = context;
    }

    public OnClassItemClick listener;

    public void setListener(OnClassItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_class, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GoodsClass goodsClass) {
        holder.classText.setText(goodsClass.getClassName());

        Glide.with(context).load(goodsClass.getClassImage()).into(holder.classImageView);
        RxViewAction.clickNoDouble(holder.goodsClassLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onClassItemClikcListener(goodsClass.getClassId(),goodsClass.getClassName());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView classText;
        private final ImageView classImageView;
        private final FrameLayout goodsClassLayout;

        ViewHolder(View itemView) {
            super(itemView);
            classText = ((TextView) itemView.findViewById(R.id.class_text));
            classImageView = ((ImageView) itemView.findViewById(R.id.class_image));
            goodsClassLayout = ((FrameLayout) itemView.findViewById(R.id.goods_class_latyout));
        }
    }

    public interface OnClassItemClick{
        void onClassItemClikcListener(int classId,String className);
    }
}