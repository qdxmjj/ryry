package com.example.warehouse.ui.mulit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.warehouse.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

/**
 * Created by Lenovo on 2018/11/20.
 */
public class TireSizeViewBinder extends ItemViewProvider<TireSize, TireSizeViewBinder.ViewHolder> {
    public OnTireSizeClickListenr listenr;

    public void setListenr(OnTireSizeClickListenr listenr) {
        this.listenr = listenr;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tire_size, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TireSize tireSize) {
        holder.tireSizeView.setText(tireSize.getSize());
        holder.tireFlgureView.setText(tireSize.getFlgureName());
        holder.tireCountView.setText(tireSize.getCount() +"个");
        RxViewAction.clickNoDouble(holder.tireCountView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listenr.onTireCountClickListener(tireSize.getFlgureName(),tireSize.getId());
                    }
                });
        RxViewAction.clickNoDouble(holder.tireDeleteView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listenr.onTireDeleteClickListener(tireSize.getFlgureName(),tireSize.getId());
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tireSizeView;
        private final TextView tireFlgureView;
        private final TextView tireCountView;
        private final ImageView tireDeleteView;

        ViewHolder(View itemView) {
            super(itemView);
            tireSizeView = ((TextView) itemView.findViewById(R.id.tire_size_view));
            tireFlgureView = ((TextView) itemView.findViewById(R.id.tire_flgure_view));
            tireCountView = ((TextView) itemView.findViewById(R.id.tire_count_view));
            tireDeleteView = ((ImageView) itemView.findViewById(R.id.tire_delete_view));
        }
    }

    public interface OnTireSizeClickListenr{
        void onTireCountClickListener(String flgureName,int id);
        void onTireDeleteClickListener(String flgureName,int id);
    }
}
