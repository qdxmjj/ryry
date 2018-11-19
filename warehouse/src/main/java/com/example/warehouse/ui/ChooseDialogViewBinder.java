package com.example.warehouse.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.warehouse.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

/**
 * Created by Lenovo on 2018/11/16.
 */
public class ChooseDialogViewBinder extends ItemViewProvider<ChooseDialog, ChooseDialogViewBinder.ViewHolder> {
    public onDialogChooseClick listener;

    public void setListener(onDialogChooseClick listener) {
        this.listener = listener;
    }

    private static final String TAG = ChooseDialogViewBinder.class.getSimpleName();

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_choose_dialog, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ChooseDialog chooseDialog) {
        Log.e(TAG, "onBindViewHolder: " + chooseDialog.getName() );
        holder.chooseNameView.setText(chooseDialog.getName());
        RxViewAction.clickNoDouble(holder.chooseNameView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onChooseItemClickListener(chooseDialog.getName());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView chooseNameView;

        ViewHolder(View itemView) {
            super(itemView);
            chooseNameView = ((TextView) itemView.findViewById(R.id.choose_name_view));
        }
    }

    public interface onDialogChooseClick{
        void onChooseItemClickListener(String name);
    }
}
