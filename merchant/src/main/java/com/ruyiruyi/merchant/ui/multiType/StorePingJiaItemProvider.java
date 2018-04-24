package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsBean;
import com.ruyiruyi.merchant.bean.StorePingJiaBean;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import me.drakeet.multitype.ItemViewProvider;

public class StorePingJiaItemProvider  extends ItemViewProvider<StorePingJiaBean, StorePingJiaItemProvider.ViewHolder> {

    private Context context;
    private RequestManager mGlideReqManager;

    public StorePingJiaItemProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_store_pingjia, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StorePingJiaBean storePingJiaBean) {
        holder.tv_user_name.setText(storePingJiaBean.getUser_name());
        holder.tv_pingjia_time.setText(storePingJiaBean.getPj_time());
        holder.tv_pingjia_txt.setText(storePingJiaBean.getPj_txt());

        //glide 设置圆形图片
        String topimgurl = storePingJiaBean.getUser_img_url();
        mGlideReqManager = Glide.with(context);
        mGlideReqManager.load(topimgurl).transform(new GlideCircleTransform(  context  )).into(holder.img_user);
        //设置评价图片 若后台获取null 则隐藏；

        ImageOptions options = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(3))
                .setSquare(true)// setSquare和build为必须设置
                .build();
        if (storePingJiaBean.getPingjia_pica_url() != null) {
            x.image().bind(holder.img_pingjia_a, storePingJiaBean.getPingjia_pica_url(),options);
        } else {
            holder.img_pingjia_a.setVisibility(View.GONE);
        }
        if (storePingJiaBean.getPingjia_picb_url() != null) {
            x.image().bind(holder.img_pingjia_b, storePingJiaBean.getPingjia_picb_url(),options);
        } else {
            holder.img_pingjia_b.setVisibility(View.GONE);
        }
        if (storePingJiaBean.getPingjia_picc_url() != null) {
            x.image().bind(holder.img_pingjia_c, storePingJiaBean.getPingjia_picc_url(),options);
        } else {
            holder.img_pingjia_c.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_user;
        private final TextView tv_user_name;
        private final TextView tv_pingjia_time;
        private final TextView tv_pingjia_txt;
        private final ImageView img_pingjia_a;
        private final ImageView img_pingjia_b;
        private final ImageView img_pingjia_c;

        ViewHolder(View itemView) {
            super(itemView);
            img_user = ((ImageView) itemView.findViewById(R.id.img_user));
            tv_user_name = ((TextView) itemView.findViewById(R.id.tv_user_name));
            tv_pingjia_time = ((TextView) itemView.findViewById(R.id.tv_pingjia_time));
            tv_pingjia_txt = ((TextView) itemView.findViewById(R.id.tv_pingjia_txt));
            img_pingjia_a = ((ImageView) itemView.findViewById(R.id.img_pingjia_a));
            img_pingjia_b = ((ImageView) itemView.findViewById(R.id.img_pingjia_b));
            img_pingjia_c = ((ImageView) itemView.findViewById(R.id.img_pingjia_c));
        }
    }
}