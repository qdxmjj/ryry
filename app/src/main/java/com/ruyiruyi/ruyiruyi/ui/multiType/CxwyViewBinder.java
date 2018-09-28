package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class CxwyViewBinder extends ItemViewProvider<Cxwy, CxwyViewBinder.ViewHolder> {

    public OnChooseClickListener listener;

    public void setListener(OnChooseClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_cxwy, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Cxwy cxwy) {
        if (cxwy.getCxwyType() == 2) {  //1是赠送  2是购买
            holder.cxwyLayout.setBackgroundResource(R.drawable.cxwy_normal);
            holder.cxwyTimeText.setText("限制使用时间： 车辆服务年限内有效");
        //    holder.cxwyTimeText.setText("限制使用时间： " + cxwy.getCxwyStartTime() + "至" + cxwy.getCxwyEndTime());
        }else if (cxwy.getCxwyType() == 1){
            holder.cxwyLayout.setBackgroundResource(R.drawable.cxwy_give);
            holder.cxwyTimeText.setText("限制使用时间： 车辆服务年限内有效");
         //   holder.cxwyTimeText.setText("限制使用时间： " + cxwy.getCxwyStartTime() + "至" + cxwy.getCxwyEndTime());
        }
        if (cxwy.isChoose()){
            holder.cxwyChooseImage.setImageResource(R.drawable.ic_choose);
        }else {
            holder.cxwyChooseImage.setImageResource(R.drawable.ic_choose_no);
        }

        RxViewAction.clickNoDouble(holder.chooseLinearLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onChooseItemClick(cxwy.getCxwyId());
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout cxwyLayout;
        private final TextView cxwyTimeText;
        private final ImageView cxwyChooseImage;
        private final LinearLayout chooseLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            cxwyLayout = ((FrameLayout) itemView.findViewById(R.id.cxwy_layout));
            cxwyTimeText = ((TextView) itemView.findViewById(R.id.cxwy_time_text));
            cxwyChooseImage = ((ImageView) itemView.findViewById(R.id.cxwy_choose_imageview));
            chooseLinearLayout = ((LinearLayout) itemView.findViewById(R.id.choose_layout));
        }
    }

    public interface OnChooseClickListener{
        void onChooseItemClick(int cxwyId);
    }
}