package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.StorePingJiaBean;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class StorePingJiaItemProvider extends ItemViewProvider<StorePingJiaBean, StorePingJiaItemProvider.ViewHolder> {

    private Context context;
    private RequestManager mGlideReqManager;
    private int hasPic_a_ = 0;//0 没有  1 有
    private int hasPic_b_ = 0;//0 没有  1 有
    private int hasPic_c_ = 0;//0 没有  1 有
    private int hasPic_d_ = 0;//0 没有  1 有
    private int hasPic_e_ = 0;//0 没有  1 有

    private OnPingjiaPicClick listener;

    public void setListener(OnPingjiaPicClick listener) {
        this.listener = listener;
    }

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final StorePingJiaBean storePingJiaBean) {
        holder.tv_user_name.setText(storePingJiaBean.getUser_name());
        holder.tv_pingjia_time.setText(storePingJiaBean.getPj_time());
        holder.tv_pingjia_txt.setText(storePingJiaBean.getPj_txt());

        //glide 设置圆形图片
        String topimgurl = storePingJiaBean.getUser_img_url();
        mGlideReqManager = Glide.with(context);
        mGlideReqManager.load(topimgurl)
                .transform(new GlideCircleTransform(context))
                .into(holder.img_user);
        //设置评价图片 若后台获取null 则隐藏；

        ImageOptions options = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(3))
                .setSquare(true)// setSquare和build为必须设置
                .build();
        if (storePingJiaBean.getPingjia_pica_url() != null && storePingJiaBean.getPingjia_pica_url().length() != 0) {
            hasPic_a_ = 1;
            x.image().bind(holder.img_pingjia_a, storePingJiaBean.getPingjia_pica_url(), options);
        } else {
            holder.img_pingjia_a.setVisibility(View.GONE);
        }
        if (storePingJiaBean.getPingjia_picb_url() != null && storePingJiaBean.getPingjia_picb_url().length() != 0) {
            hasPic_b_ = 1;
            x.image().bind(holder.img_pingjia_b, storePingJiaBean.getPingjia_picb_url(), options);
        } else {
            holder.img_pingjia_b.setVisibility(View.GONE);
        }
        if (storePingJiaBean.getPingjia_picc_url() != null && storePingJiaBean.getPingjia_picc_url().length() != 0) {
            hasPic_c_ = 1;
            x.image().bind(holder.img_pingjia_c, storePingJiaBean.getPingjia_picc_url(), options);
        } else {
            holder.img_pingjia_c.setVisibility(View.GONE);
        }
        if (storePingJiaBean.getPingjia_picd_url() != null && storePingJiaBean.getPingjia_picd_url().length() != 0) {
            hasPic_d_ = 1;
            x.image().bind(holder.img_pingjia_d, storePingJiaBean.getPingjia_picd_url(), options);
        } else {
            holder.img_pingjia_d.setVisibility(View.GONE);
        }
        if (storePingJiaBean.getPingjia_pice_url() != null && storePingJiaBean.getPingjia_pice_url().length() != 0) {
            hasPic_e_ = 1;
            x.image().bind(holder.img_pingjia_e, storePingJiaBean.getPingjia_pice_url(), options);
        } else {
            holder.img_pingjia_e.setVisibility(View.GONE);
        }
        if (hasPic_a_ == 0 && hasPic_b_ == 0 && hasPic_c_ == 0 && hasPic_d_ == 0 && hasPic_e_ == 0) {
            holder.ll_pingjia_pics.setVisibility(View.GONE);
            Log.e("GONE?", "onBindViewHolder:GONE? ");
        }


        //绑定图片查看监听
        RxViewAction.clickNoDouble(holder.img_pingjia_a).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String pingjia_pica_url = storePingJiaBean.getPingjia_pica_url();
                int pj_id = storePingJiaBean.getPingjia_id();
                listener.onPingjiaPicClickListener(pingjia_pica_url, pj_id);
            }
        });
        RxViewAction.clickNoDouble(holder.img_pingjia_b).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String pingjia_picb_url = storePingJiaBean.getPingjia_picb_url();
                int pj_id = storePingJiaBean.getPingjia_id();
                listener.onPingjiaPicClickListener(pingjia_picb_url, pj_id);
            }
        });
        RxViewAction.clickNoDouble(holder.img_pingjia_c).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String pingjia_picc_url = storePingJiaBean.getPingjia_picc_url();
                int pj_id = storePingJiaBean.getPingjia_id();
                listener.onPingjiaPicClickListener(pingjia_picc_url, pj_id);
            }
        });
        RxViewAction.clickNoDouble(holder.img_pingjia_d).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String pingjia_picd_url = storePingJiaBean.getPingjia_picd_url();
                int pj_id = storePingJiaBean.getPingjia_id();
                listener.onPingjiaPicClickListener(pingjia_picd_url, pj_id);
            }
        });
        RxViewAction.clickNoDouble(holder.img_pingjia_e).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String pingjia_pice_url = storePingJiaBean.getPingjia_pice_url();
                int pj_id = storePingJiaBean.getPingjia_id();
                listener.onPingjiaPicClickListener(pingjia_pice_url, pj_id);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_user;
        private final TextView tv_user_name;
        private final TextView tv_pingjia_time;
        private final TextView tv_pingjia_txt;
        private final ImageView img_pingjia_a;
        private final ImageView img_pingjia_b;
        private final ImageView img_pingjia_c;
        private final ImageView img_pingjia_d;
        private final ImageView img_pingjia_e;
        private final LinearLayout ll_pingjia_pics;

        ViewHolder(View itemView) {
            super(itemView);
            img_user = ((ImageView) itemView.findViewById(R.id.img_user));
            tv_user_name = ((TextView) itemView.findViewById(R.id.tv_user_name));
            tv_pingjia_time = ((TextView) itemView.findViewById(R.id.tv_pingjia_time));
            tv_pingjia_txt = ((TextView) itemView.findViewById(R.id.tv_pingjia_txt));
            img_pingjia_a = ((ImageView) itemView.findViewById(R.id.img_pingjia_a));
            img_pingjia_b = ((ImageView) itemView.findViewById(R.id.img_pingjia_b));
            img_pingjia_c = ((ImageView) itemView.findViewById(R.id.img_pingjia_c));
            img_pingjia_d = ((ImageView) itemView.findViewById(R.id.img_pingjia_d));
            img_pingjia_e = ((ImageView) itemView.findViewById(R.id.img_pingjia_e));
            ll_pingjia_pics = ((LinearLayout) itemView.findViewById(R.id.ll_pingjia_pics));
        }
    }

    public interface OnPingjiaPicClick {
        void onPingjiaPicClickListener(String url, int pjId);
    }
}