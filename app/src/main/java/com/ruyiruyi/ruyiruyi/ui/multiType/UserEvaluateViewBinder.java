package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.utils.FullyLinearLayoutManager;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.NewRatingBar;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class UserEvaluateViewBinder extends ItemViewProvider<UserEvaluate, UserEvaluateViewBinder.ViewHolder> {
    public Context context;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public OnImageItemClick listener;
    public int evaluateType;

    public void setEvaluateType(int evaluateType) {//0是门店评价  1是我的评价
        this.evaluateType = evaluateType;
    }

    public void setListener(OnImageItemClick listener) {
        this.listener = listener;
    }

    public UserEvaluateViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_user_evaluate, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final UserEvaluate userEvaluate) {
        Glide.with(context).load(userEvaluate.getUserImage()).transform(new GlideCircleTransform(context)).into(holder.userImageView);
        holder.usernameText.setText(userEvaluate.getUsetName());
        holder.evaluateTimeText.setText(userEvaluate.getEvaluateTime());
        holder.evaluateContentText.setText(userEvaluate.getEvaluateContent());
        int size = userEvaluate.getEvaluateImageList().size();
        if (size == 0){
            holder.pingjiaImageLayout.setVisibility(View.GONE);
        }else if (size == 1){
            holder.pingjiaImageLayout.setVisibility(View.VISIBLE);
            holder.imageViewA.setVisibility(View.VISIBLE);
            holder.imageViewB.setVisibility(View.INVISIBLE);
            holder.imageViewC.setVisibility(View.INVISIBLE);
            holder.imageViewD.setVisibility(View.INVISIBLE);
            holder.imageViewE.setVisibility(View.INVISIBLE);
        }else if (size == 2){
            holder.pingjiaImageLayout.setVisibility(View.VISIBLE);
            holder.imageViewA.setVisibility(View.VISIBLE);
            holder.imageViewB.setVisibility(View.VISIBLE);
            holder.imageViewC.setVisibility(View.INVISIBLE);
            holder.imageViewD.setVisibility(View.INVISIBLE);
            holder.imageViewE.setVisibility(View.INVISIBLE);
        }else if (size == 3){
            holder.pingjiaImageLayout.setVisibility(View.VISIBLE);
            holder.imageViewA.setVisibility(View.VISIBLE);
            holder.imageViewB.setVisibility(View.VISIBLE);
            holder.imageViewC.setVisibility(View.VISIBLE);
            holder.imageViewD.setVisibility(View.INVISIBLE);
            holder.imageViewE.setVisibility(View.INVISIBLE);
        }else if (size == 4){
            holder.pingjiaImageLayout.setVisibility(View.VISIBLE);
            holder.imageViewA.setVisibility(View.VISIBLE);
            holder.imageViewB.setVisibility(View.VISIBLE);
            holder.imageViewC.setVisibility(View.VISIBLE);
            holder.imageViewD.setVisibility(View.VISIBLE);
            holder.imageViewE.setVisibility(View.INVISIBLE);
        }else if (size == 5){
            holder.pingjiaImageLayout.setVisibility(View.VISIBLE);
            holder.imageViewA.setVisibility(View.VISIBLE);
            holder.imageViewB.setVisibility(View.VISIBLE);
            holder.imageViewC.setVisibility(View.VISIBLE);
            holder.imageViewD.setVisibility(View.VISIBLE);
            holder.imageViewE.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < size; i++) {
            if (i==0){
                Glide.with(context).load(userEvaluate.getEvaluateImageList().get(i)).into(holder.imageViewA);
            }else if (i == 1){
                Glide.with(context).load(userEvaluate.getEvaluateImageList().get(i)).into(holder.imageViewB);
            }else if (i == 2){
                Glide.with(context).load(userEvaluate.getEvaluateImageList().get(i)).into(holder.imageViewC);
            }else if (i == 3){
                Glide.with(context).load(userEvaluate.getEvaluateImageList().get(i)).into(holder.imageViewD);
            }else if (i == 4){
                Glide.with(context).load(userEvaluate.getEvaluateImageList().get(i)).into(holder.imageViewE);
            }

        }

        RxViewAction.clickNoDouble(holder.imageViewA)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageItemClickListener(userEvaluate.getEvaluateImageList().get(0),userEvaluate.getEvaluateId());
                    }
                });
        RxViewAction.clickNoDouble(holder.imageViewB)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageItemClickListener(userEvaluate.getEvaluateImageList().get(1),userEvaluate.getEvaluateId());
                    }
                });
        RxViewAction.clickNoDouble(holder.imageViewC)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageItemClickListener(userEvaluate.getEvaluateImageList().get(2),userEvaluate.getEvaluateId());
                    }
                });
        RxViewAction.clickNoDouble(holder.imageViewD)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageItemClickListener(userEvaluate.getEvaluateImageList().get(3),userEvaluate.getEvaluateId());
                    }
                });
        RxViewAction.clickNoDouble(holder.imageViewE)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageItemClickListener(userEvaluate.getEvaluateImageList().get(4),userEvaluate.getEvaluateId());
                    }
                });

        holder.ratingBar.setClickable(false);
        holder.ratingBar.setStar(userEvaluate.getStarNo());

  /*      GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);
        if (userEvaluate.getEvaluateImageList().size()>0){
            holder.listView.setVisibility(View.VISIBLE);
            initData(userEvaluate.getEvaluateImageList(),userEvaluate.getEvaluateId());
        }else {
            holder.listView.setVisibility(View.GONE);
          //  initData(userEvaluate.getEvaluateImageList(),userEvaluate.getEvaluateId());
        }*/
        holder.storeNameView.setText(userEvaluate.getStoreName());
        holder.storeAddressView.setText("地址：" + userEvaluate.getStoreAddress());
        Glide.with(context).load(userEvaluate.getStoreImage()).into(holder.storeImageView);

        if (evaluateType == 0){
            holder.shopLayout.setVisibility(View.GONE);
        }else {
            holder.shopLayout.setVisibility(View.VISIBLE);
        }


    }



    static class ViewHolder extends RecyclerView.ViewHolder {

    //    private final RecyclerView listView;
        private final TextView evaluateContentText;
        private final TextView usernameText;
        private final TextView evaluateTimeText;
        private final ImageView userImageView;
        private final ImageView imageViewA;
        private final ImageView imageViewB;
        private final ImageView imageViewC;
        private final ImageView imageViewD;
        private final ImageView imageViewE;
        private final LinearLayout pingjiaImageLayout;
        private final NewRatingBar ratingBar;
        private final TextView storeNameView;
        private final TextView storeAddressView;
        private final ImageView storeImageView;
        private final LinearLayout shopLayout;

        ViewHolder(View itemView) {
            super(itemView);
          //  listView = ((RecyclerView) itemView.findViewById(R.id.phote_imageview));
            evaluateContentText = ((TextView) itemView.findViewById(R.id.evaluate_content_text));
            usernameText = ((TextView) itemView.findViewById(R.id.username_text));
            evaluateTimeText = ((TextView) itemView.findViewById(R.id.evaluate_time_text));
            userImageView = ((ImageView) itemView.findViewById(R.id.user_image));
            imageViewA = ((ImageView) itemView.findViewById(R.id.img_pingjia_a));
            imageViewB = ((ImageView) itemView.findViewById(R.id.img_pingjia_b));
            imageViewC = ((ImageView) itemView.findViewById(R.id.img_pingjia_c));
            imageViewD = ((ImageView) itemView.findViewById(R.id.img_pingjia_d));
            imageViewE = ((ImageView) itemView.findViewById(R.id.img_pingjia_e));
            pingjiaImageLayout = (LinearLayout) itemView.findViewById(R.id.ll_pingjia_pics);
            storeNameView = ((TextView) itemView.findViewById(R.id.store_name_view));
            storeAddressView = (TextView) itemView.findViewById(R.id.store_address_view);
            storeImageView = ((ImageView) itemView.findViewById(R.id.store_image_view));
            ratingBar = (NewRatingBar) itemView.findViewById(R.id.rating_bar);
            shopLayout = (LinearLayout) itemView.findViewById(R.id.shop_layout);

        }
    }

    public interface OnImageItemClick{
        void onImageItemClickListener(String url, int evaluateId);
    }

}