package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ButtonViewBinder extends ItemViewProvider<Button, ButtonViewBinder.ViewHolder> {

    public OnButtonClick listener;

    public void setListener(OnButtonClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_button, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Button button) {
        RxViewAction.clickNoDouble(holder.buttonView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onButtonClickListener();
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView buttonView;

        ViewHolder(View itemView) {
            super(itemView);
            buttonView = ((TextView) itemView.findViewById(R.id.button_view));
        }
    }

    public interface OnButtonClick{
        void onButtonClickListener();
    }
}