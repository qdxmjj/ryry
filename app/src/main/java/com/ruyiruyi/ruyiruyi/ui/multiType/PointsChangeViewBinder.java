package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.PointsGoodsInfoActivity;
import com.ruyiruyi.ruyiruyi.utils.CircleImageView;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class PointsChangeViewBinder extends ItemViewProvider<PointsChange, PointsChangeViewBinder.ViewHolder> {
    private Context mContext;

    public PointsChangeViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_pointschange, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PointsChange pointsChange) {
        Glide.with(mContext).load(pointsChange.getGoodsPic()).into(holder.civ);
        holder.tv_title.setText(pointsChange.getTitle());
        holder.tv_points.setText(pointsChange.getPoints() + "");
        holder.tv_price.setText(pointsChange.getPrice() + "");
        holder.tv_changenum.setText(pointsChange.getChangeNum() + "");

        RxViewAction.clickNoDouble(holder.civ).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mContext, PointsGoodsInfoActivity.class);
                intent.putExtra("goodsId", pointsChange.getGoodsId());
                intent.putExtra("goodsPic", pointsChange.getGoodsPic());
                intent.putExtra("title", pointsChange.getTitle());
                intent.putExtra("points", pointsChange.getPoints());
                intent.putExtra("price", pointsChange.getPrice());
                intent.putExtra("changeNum", pointsChange.getChangeNum());
                mContext.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView civ;
        private final TextView tv_title;
        private final TextView tv_points;
        private final TextView tv_price;
        private final TextView tv_changenum;

        ViewHolder(View itemView) {
            super(itemView);

            civ = (CircleImageView) itemView.findViewById(R.id.civ);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_points = (TextView) itemView.findViewById(R.id.tv_points);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_changenum = (TextView) itemView.findViewById(R.id.tv_changenum);
        }
    }

}