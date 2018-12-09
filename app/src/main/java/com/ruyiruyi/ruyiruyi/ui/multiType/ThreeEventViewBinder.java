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

public class ThreeEventViewBinder extends ItemViewProvider<ThreeEvent, ThreeEventViewBinder.ViewHolder> {
    private Context context;

    public ThreeEventViewBinder(Context context) {
        this.context = context;
    }

    public OneEventViewBinder.OnEventClick listener;

    public void setListener(OneEventViewBinder.OnEventClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_three_event, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ThreeEvent threeEvent) {
        Glide.with(context).load(threeEvent.getLeftEvent().getImageUrl()).into(holder.leftImageView);
        Glide.with(context).load(threeEvent.getRightOneEvent().getImageUrl()).into(holder.rightOneView);
        Glide.with(context).load(threeEvent.getRightTwoEvent().getImageUrl()).into(holder.rightTwoView);


        RxViewAction.clickNoDouble(holder.leftImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOneEventClickListener(threeEvent.getLeftEvent().getSkip(),threeEvent.getLeftEvent().getContent(),threeEvent.getLeftEvent().getWebUrl(),threeEvent.getLeftEvent().getStockId(),threeEvent.getLeftEvent().getServiceId());
                    }
                });
        RxViewAction.clickNoDouble(holder.rightOneView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOneEventClickListener(threeEvent.getRightOneEvent().getSkip(),threeEvent.getRightOneEvent().getContent(),threeEvent.getRightOneEvent().getWebUrl(),threeEvent.getRightOneEvent().getStockId(),threeEvent.getRightOneEvent().getServiceId());
                    }
                });
        RxViewAction.clickNoDouble(holder.rightTwoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOneEventClickListener(threeEvent.getRightTwoEvent().getSkip(),threeEvent.getRightTwoEvent().getContent(),threeEvent.getRightTwoEvent().getWebUrl(),threeEvent.getRightTwoEvent().getStockId(),threeEvent.getRightTwoEvent().getServiceId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView leftImageView;
        private final ImageView rightOneView;
        private final ImageView rightTwoView;

        ViewHolder(View itemView) {
            super(itemView);
            leftImageView = ((ImageView) itemView.findViewById(R.id.left_image));
            rightOneView = ((ImageView) itemView.findViewById(R.id.right_one_image));
            rightTwoView = ((ImageView) itemView.findViewById(R.id.right_two_image));
        }
    }

}