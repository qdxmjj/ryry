package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class EvaluateImageViewBinder extends ItemViewProvider<EvaluateImage, EvaluateImageViewBinder.ViewHolder> {
    public Context context;
    public OnEvaluateImageClickListener listener;

    public void setListener(OnEvaluateImageClickListener listener) {
        this.listener = listener;
    }

    public EvaluateImageViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_evaluate_image, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final EvaluateImage evaluateImage) {
        if (evaluateImage.isAdd.booleanValue()){
            holder.imageView.setImageResource(R.drawable.ic_bigphoto);
            holder.imageDelete.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(evaluateImage.getUri()).into(holder.imageView);
            holder.imageDelete.setVisibility(View.VISIBLE);
        }

        RxViewAction.clickNoDouble(holder.imageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageAddClickListener(evaluateImage.isAdd.booleanValue(),evaluateImage.getUri());
                    }
                });
        RxViewAction.clickNoDouble(holder.imageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageDelete(evaluateImage.getUri());
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageView imageDelete;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.evaluate_item_image));
            imageDelete = ((ImageView) itemView.findViewById(R.id.evaluate_item_delete));

        }
    }

    public interface OnEvaluateImageClickListener {
        void onImageAddClickListener(boolean var1, Uri var2);
        void onImageDelete(Uri var1);
    }

}