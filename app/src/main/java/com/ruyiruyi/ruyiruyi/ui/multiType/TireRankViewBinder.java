package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.listener.OnFigureItemInterface;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class TireRankViewBinder extends ItemViewProvider<TireRank, TireRankViewBinder.ViewHolder> {
    /*public OnTireRankClick listener;

    public void setListener(OnTireRankClick listener) {
        this.listener = listener;
    }*/
    public OnFigureItemInterface listener;

    public void setListener(OnFigureItemInterface listener) {
        this.listener = listener;
    }
   /* public TireFigureViewBinder.OnFigureItemClick listenter;


    public void setListenter(TireFigureViewBinder.OnFigureItemClick listenter) {
        this.listenter = listenter;
    }*/

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tire_rank, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TireRank tireRank) {
        holder.tireRankText.setText(tireRank.getRankName());
        if (tireRank.isCheck){
            holder.tireRankText.setBackgroundResource(R.drawable.bg_price_click);
            holder.tireRankText.setTextColor(Color.WHITE );
        }else {
            holder.tireRankText.setBackgroundResource(R.drawable.bg_price_no_click);
            holder.tireRankText.setTextColor(Color.rgb(100, 100, 100));
        }


        RxViewAction.clickNoDouble(holder.tireRankText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                      //  listener.onTireRankClickListener(tireRank.getRankName());
                        listener.onRankClickListener(tireRank.getRankName(),tireRank.getFigureName());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tireRankText;

        ViewHolder(View itemView) {
            super(itemView);
            tireRankText = ((TextView) itemView.findViewById(R.id.tire_rank_text));
        }
    }

    /*public interface OnTireRankClick{
        void onTireRankClickListener(String name);
    }*/
}