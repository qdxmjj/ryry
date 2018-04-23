package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.listener.OnFigureItemInterface;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class TireFigureViewBinder extends ItemViewProvider<TireFigure, TireFigureViewBinder.ViewHolder> {
    private Context context;
   // public OnFigureItemClick listener;
    //public TireRankViewBinder.OnTireRankClick ranklistener;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<TireRank> tireRankList;
    public OnFigureItemInterface listener;

    public void setListener(OnFigureItemInterface listener) {
        this.listener = listener;
    }

    /*public void setRanklistener(TireRankViewBinder.OnTireRankClick ranklistener) {
        TireRankViewBinder.OnTireRankClic
    }*/

  /*  public void setListener(OnFigureItemClick listener) {
        this.listener = listener;
    }*/

    public void setContext(Context context) {
        this.context = context;
    }

    public TireFigureViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tire_figure, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final TireFigure tireFigure) {
        if (tireFigure.isCheck) {
            holder.contentLayout.setVisibility(View.VISIBLE);
            holder.titleLayout.setBackgroundResource(R.color.theme_primary);
            holder.titleImage.setImageResource(R.drawable.ic_down);
            holder.titleName.setTextColor(Color.WHITE);
        }else {
            holder.contentLayout.setVisibility(View.GONE);
            holder.titleLayout.setBackgroundColor(Color.WHITE);
            holder.titleImage.setImageResource(R.drawable.ic_right_car);
            holder.titleName.setTextColor(Color.rgb(100, 100, 100));
        }
        holder.titleName.setText(tireFigure.getTitleStr());
        Glide.with(context).load(tireFigure.getOneImage()).into(holder.oneImage);
        Glide.with(context).load(tireFigure.getTwoImage()).into(holder.twoImage);
        Glide.with(context).load(tireFigure.getThreeImage()).into(holder.threeImage);
        Glide.with(context).load(tireFigure.getOneImage()).into(holder.bigImage);
        holder.contentText.setText(tireFigure.getContentStr());


        RxViewAction.clickNoDouble(holder.titleLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        listener.onFigureClickListener(tireFigure.getTitleStr());
                    }
                });
        RxViewAction.clickNoDouble(holder.oneImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Glide.with(context).load(tireFigure.getOneImage()).into(holder.bigImage);
                    }
                });
        RxViewAction.clickNoDouble(holder.twoImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Glide.with(context).load(tireFigure.getTwoImage()).into(holder.bigImage);
                    }
                });
        RxViewAction.clickNoDouble(holder.threeImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Glide.with(context).load(tireFigure.getThreeImage()).into(holder.bigImage);
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);
        tireRankList = tireFigure.getTireRankList();
        setData();

    }

    private void setData() {
        items.clear();
        for (int i = 0; i < tireRankList.size(); i++) {
            items.add(tireRankList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void register() {
        TireRankViewBinder tireRankViewBinder = new TireRankViewBinder();
        tireRankViewBinder.setListener(listener);
        adapter.register(TireRank.class, tireRankViewBinder);
    }

   /* *//**
     * 速度级别点击回掉
     *//*
    @Override
    public void onTireRankClickListener(String name) {
       listener.onRankClickListener(name);
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout titleLayout;
        private final TextView titleName;
        private final ImageView titleImage;
        private final LinearLayout contentLayout;
        private final ImageView bigImage;
        private final ImageView oneImage;
        private final ImageView twoImage;
        private final ImageView threeImage;
        private final TextView contentText;
        private final RecyclerView listView;

        ViewHolder(View itemView) {
            super(itemView);
            titleLayout = ((FrameLayout) itemView.findViewById(R.id.title_layout));
            titleName = ((TextView) itemView.findViewById(R.id.title_name_text));
            titleImage = ((ImageView) itemView.findViewById(R.id.title_image));
            contentLayout = ((LinearLayout) itemView.findViewById(R.id.content_layout));
            bigImage = ((ImageView) itemView.findViewById(R.id.big_image));
            oneImage = ((ImageView) itemView.findViewById(R.id.one_image));
            twoImage = ((ImageView) itemView.findViewById(R.id.two_image));
            threeImage = ((ImageView) itemView.findViewById(R.id.three_image));
            contentText = ((TextView) itemView.findViewById(R.id.content_text));
            listView = ((RecyclerView) itemView.findViewById(R.id.price_list));


        }
    }

   /* public interface OnFigureItemClick{
        void onFigureClickListener(String name);
        void onRankClickListener(String rankName);
    }*/
}