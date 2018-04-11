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

public class RoadChooseViewBinder extends ItemViewProvider<RoadChoose, RoadChooseViewBinder.ViewHolder> {

    public Context context;
    public OnRoadChooseClick listener;

    public void setListener(OnRoadChooseClick listener) {
        this.listener = listener;
    }

    public RoadChooseViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_road_choose, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final RoadChoose roadChoose) {
        if (roadChoose.isChoose){
            holder.roadChoose.setVisibility(View.VISIBLE);
        }else {
            holder.roadChoose.setVisibility(View.GONE);
        }
        Glide.with(context).load(roadChoose.imageUrl).into(holder.roadImage);
        holder.roadTitle.setText(roadChoose.getTitle());
        holder.roadContent.setText(roadChoose.getContent());

        RxViewAction.clickNoDouble(holder.roadLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onRoadChossClickListener(roadChoose.roadId,!roadChoose.isChoose);
                    }
                });



    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView roadChoose;
        private final ImageView roadImage;
        private final TextView roadTitle;
        private final TextView roadContent;
        private final FrameLayout roadLayout;

        ViewHolder(View itemView) {
            super(itemView);
            roadChoose = ((ImageView) itemView.findViewById(R.id.road_choose));
            roadImage = ((ImageView) itemView.findViewById(R.id.road_image));
            roadTitle = ((TextView) itemView.findViewById(R.id.road_title));
            roadContent = ((TextView) itemView.findViewById(R.id.road_content));
            roadLayout = ((FrameLayout) itemView.findViewById(R.id.road_layout));
        }
    }

    public interface OnRoadChooseClick{
        void onRoadChossClickListener(int id,boolean choose);
    }
}