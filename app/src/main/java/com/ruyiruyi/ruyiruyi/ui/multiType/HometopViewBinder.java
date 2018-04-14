package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import org.w3c.dom.Text;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class HometopViewBinder extends ItemViewProvider<Hometop, HometopViewBinder.ViewHolder> {

    public Context context;
    public OnHomeTopItemClickListener listener;

    public HometopViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(OnHomeTopItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_top, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Hometop hometop) {
        holder.mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        },hometop.imageList)//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);

        holder.carTitle.setText(hometop.carTitle);
        holder.carContent.setText(hometop.carContent);
        if (hometop.state == 0){    //未登陆
            holder.carImage.setImageResource(R.drawable.ic_one_register);
        }else if (hometop.state == 1){  //未添加车辆
            holder.carImage.setImageResource(R.drawable.ic_add );
        }else {     //已添加车辆
            Glide.with(context).load(hometop.carImage).into(holder.carImage);

        }

        RxViewAction.clickNoDouble(holder.cityLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCityLayoutClickListener();
                    }
                });
        RxViewAction.clickNoDouble(holder.carLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCarItemClickListener(hometop.state);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView carImage;
        private final TextView carTitle;
        private final TextView carContent;
        private final CustomBanner mBanner;
        private final LinearLayout cityLayout;
        private final LinearLayout carLayout;

        ViewHolder(View itemView) {
            super(itemView);
            mBanner = ((CustomBanner) itemView.findViewById(R.id.banner));
            carImage = ((ImageView) itemView.findViewById(R.id.home_car_image));
            carTitle = ((TextView) itemView.findViewById(R.id.home_car_title));
            carContent = ((TextView) itemView.findViewById(R.id.home_car_content));
            cityLayout = ((LinearLayout) itemView.findViewById(R.id.city_layout));
            carLayout = ((LinearLayout) itemView.findViewById(R.id.car_layout));
        }
    }

    public interface OnHomeTopItemClickListener{
        void onCityLayoutClickListener();
        void onCarItemClickListener(int state);
    }
}