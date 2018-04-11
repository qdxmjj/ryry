package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class CarFactoryViewBinder extends ItemViewProvider<CarFactoryM, CarFactoryViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_car_factory, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CarFactoryM carFactory) {
        holder.carFactory.setText(carFactory.getCarFractory());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView carFactory;

        ViewHolder(View itemView) {
            super(itemView);
            carFactory = ((TextView) itemView.findViewById(R.id.car_factory));
        }
    }
}