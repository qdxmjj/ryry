package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsItemViewBinder extends ItemViewProvider<GoodsItem, GoodsItemViewBinder.ViewHolder> {
    private static final String TAG = GoodsItemViewBinder.class.getSimpleName();
    public Context context;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public OnGoodsItemClick listener;

    public void setListener(OnGoodsItemClick listener) {
        this.listener = listener;
    }

    public GoodsItemViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GoodsItem goodsItem) {
        holder.goodClassName.setText(goodsItem.getGoodClassName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);
        if (goodsItem.getChooseGood()) {
            holder.goodIsChoose.setImageResource( R.drawable.ic_xuanzhong );
            holder.priceText.setText("￥" + goodsItem.getPrice());
            holder.priceText.setVisibility(View.VISIBLE);
            holder.listView.setVisibility(View.VISIBLE);
            Log.e(TAG, "onBindViewHolder:----- " + goodsItem.getGoodsList().size());
            initData(goodsItem.getGoodsList());
        }else {
            holder.goodIsChoose.setImageResource(R.drawable.ic_weixuan );
            holder.priceText.setVisibility(View.GONE);
            holder.listView.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(holder.goodsClassLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (goodsItem.getChooseGood()){
                            listener.onGoodsItemClickListenner(goodsItem.getGoodsClassId(),goodsItem.getGoodsList());
                        }else {
                            List<GoodsHorizontal> horizontalList = new ArrayList<GoodsHorizontal>();
                            listener.onGoodsItemClickListenner(goodsItem.getGoodsClassId(),horizontalList);
                        }

                    }
                });
    }

    private void initData(List<GoodsHorizontal> goodsHorizontalList) {
        items.clear();
        for (int i = 0; i < goodsHorizontalList.size(); i++) {
            items.add(goodsHorizontalList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void register() {
        adapter.register(GoodsHorizontal.class,new GoodsHorizontalViewBinder(context));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView goodClassName;
        private final ImageView goodIsChoose;
        private final RecyclerView listView;
        private final TextView priceText;
        private final FrameLayout goodsClassLayout;

        ViewHolder(View itemView) {
            super(itemView);
            goodClassName = ((TextView) itemView.findViewById(R.id.good_class_name_text));
            goodIsChoose = ((ImageView) itemView.findViewById(R.id.good_ischoose));
            listView = ((RecyclerView) itemView.findViewById(R.id.goods_listview));
            priceText = ((TextView) itemView.findViewById(R.id.goods_price_text));
            goodsClassLayout = ((FrameLayout) itemView.findViewById(R.id.goods_class_layout));
        }
    }

    public interface OnGoodsItemClick{
        void onGoodsItemClickListenner(int goodsClassId,List<GoodsHorizontal> goodsHorizontalList);
    }
}