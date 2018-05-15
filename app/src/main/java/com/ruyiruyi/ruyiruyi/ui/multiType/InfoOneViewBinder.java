package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class InfoOneViewBinder extends ItemViewProvider<InfoOne, InfoOneViewBinder.ViewHolder> {

    public OnInfoItemClick listener;

    public void setListener(OnInfoItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_info_one, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final InfoOne infoOne) {

        holder.titleText.setText(infoOne.getTitle());
        holder.contentText.setText(infoOne.getContent());
        holder.lineView.setVisibility(infoOne.hasLine? View.VISIBLE:View.GONE);
        holder.goImageView.setVisibility(infoOne.hasGoView ? View.VISIBLE :View.GONE);

        RxViewAction.clickNoDouble(holder.contentText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (infoOne.hasGoView){
                            listener.onInfoItemClickListener(infoOne.getTitle());
                        }
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView contentText;
        private final View lineView;
        private final ImageView goImageView;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = ((TextView) itemView.findViewById(R.id.title_info_text));
            contentText = ((TextView) itemView.findViewById(R.id.content_info_text));
            lineView = itemView.findViewById(R.id.line_view);
            goImageView = ((ImageView) itemView.findViewById(R.id.go_image_view));

        }
    }

    public interface OnInfoItemClick{
        void onInfoItemClickListener(String name);
    }
}