package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.EvaluateActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class EvaImageViewBinder extends ItemViewProvider<EvaImage, EvaImageViewBinder.ViewHolder> {
    private static final String TAG = EvaluateActivity.class.getSimpleName();
    public Context context;
    public OnEvaluateImageClick listener;

    public void setListener(OnEvaluateImageClick listener) {
        this.listener = listener;
    }

    public EvaImageViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_eva_image, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final EvaImage evaImage) {
        Log.e(TAG, "onBindViewHolder: "+evaImage.getImageUrl());

        Glide.with(context).load(evaImage.getImageUrl()).into(holder.evaluateImageView);


        RxViewAction.clickNoDouble(holder.evaluateImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onEvaluateImageClickListener(evaImage.getImageUrl(),evaImage.getEvaluateId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView evaluateImageView;

        ViewHolder(View itemView) {
            super(itemView);
            evaluateImageView = ((ImageView) itemView.findViewById(R.id.evaluate_item_image));
        }
    }
    public interface OnEvaluateImageClick{
        void onEvaluateImageClickListener(String url,int evaluateId);
    }
}