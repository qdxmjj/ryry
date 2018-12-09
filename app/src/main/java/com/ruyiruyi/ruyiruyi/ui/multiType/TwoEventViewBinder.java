package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
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


/**
 * Created by Lenovo on 2018/12/6.
 */
public class TwoEventViewBinder extends ItemViewProvider<TwoEvent, TwoEventViewBinder.ViewHolder> {
    private Context context;

    public TwoEventViewBinder(Context context) {
        this.context = context;
    }
    public OneEventViewBinder.OnEventClick listener;

    public void setListener(OneEventViewBinder.OnEventClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_two_event, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TwoEvent twoEvent) {
        Glide.with(context).load(twoEvent.getLeftEvent().getImageUrl()).into(holder.leftImageView);
        Glide.with(context).load(twoEvent.getRightEvent().getImageUrl()).into(holder.rightImageView);

        RxViewAction.clickNoDouble(holder.leftImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOneEventClickListener(twoEvent.getLeftEvent().getSkip(),twoEvent.getLeftEvent().getContent(),twoEvent.getLeftEvent().getWebUrl(),twoEvent.getLeftEvent().getStockId(),twoEvent.getLeftEvent().getServiceId());
                    }
                });
        RxViewAction.clickNoDouble(holder.rightImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOneEventClickListener(twoEvent.getRightEvent().getSkip(),twoEvent.getRightEvent().getContent(),twoEvent.getRightEvent().getWebUrl(),twoEvent.getRightEvent().getStockId(),twoEvent.getRightEvent().getServiceId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView leftImageView;
        private final ImageView rightImageView;

        ViewHolder(View itemView) {
            super(itemView);
            leftImageView = ((ImageView) itemView.findViewById(R.id.left_image_view));
            rightImageView = ((ImageView) itemView.findViewById(R.id.right_image_view));
        }
    }
}
