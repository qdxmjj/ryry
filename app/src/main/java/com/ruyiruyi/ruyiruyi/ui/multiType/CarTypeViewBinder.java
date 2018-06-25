package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class CarTypeViewBinder extends ItemViewProvider<CarType, CarTypeViewBinder.ViewHolder> {
    public OnCarTypeClick listener;

    public void setListener(OnCarTypeClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_car_type, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CarType carType) {
        if (carType.getCarType() == 0){
            holder.carPailiangLayout.setVisibility(View.GONE);
            holder.carYearLayout.setVisibility(View.GONE);
            holder.carType.setText("选择发动机排量");
        }else if (carType.getCarType() == 1){
            holder.carPailiangLayout.setVisibility(View.VISIBLE);
            holder.carPailiang.setText(carType.getPailiang());
            holder.carYearLayout.setVisibility(View.GONE);
            holder.carType.setText("请选择生产年份");
        }else {
            holder.carPailiangLayout.setVisibility(View.VISIBLE);
            holder.carPailiang.setText(carType.getPailiang());
            holder.carYearLayout.setVisibility(View.VISIBLE);
            holder.carYear.setText(carType.getYear());
            holder.carType.setText("请选择车型");
        }

        RxViewAction.clickNoDouble(holder.carPailiangLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCarPaiLiangLayoutClickListener(0);
                    }
                });
        RxViewAction.clickNoDouble(holder.carYearLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCarYearLayoutClickListener(1,carType.pailiang);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView carType;
        private final TextView carYear;
        private final LinearLayout carYearLayout;
        private final TextView carPailiang;
        private final LinearLayout carPailiangLayout;

        ViewHolder(View itemView) {
            super(itemView);
            carType = ((TextView) itemView.findViewById(R.id.cat_type_text));
            carYear = ((TextView) itemView.findViewById(R.id.car_year));
            carYearLayout = ((LinearLayout) itemView.findViewById(R.id.car_year_layout));
            carPailiang = ((TextView) itemView.findViewById(R.id.car_pailiang));
            carPailiangLayout = ((LinearLayout) itemView.findViewById(R.id.car_pailiang_layout));
        }
    }

    public interface  OnCarTypeClick{
        void onCarYearLayoutClickListener(int type,String pailiang);
        void onCarPaiLiangLayoutClickListener(int type);
    }
}