package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsItemViewBinder extends ItemViewProvider<GoodsItem, GoodsItemViewBinder.ViewHolder> {
    public Context context;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GoodsItem goodsItem) {
        holder.goodClassName.setText(goodsItem.getGoodClassName());
        holder.goodIsChoose.setImageResource(goodsItem.getChooseGood()?R.drawable.ic_xuanzhong : R.drawable.ic_weixuan);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);
        if (goodsItem.getGoodsList()!=null){
            holder.listView.setVisibility(View.VISIBLE);
            initData(goodsItem.getGoodsList());
        }else {
            holder.listView.setVisibility(View.GONE);
        }


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

        ViewHolder(View itemView) {
            super(itemView);
            goodClassName = ((TextView) itemView.findViewById(R.id.good_class_name_text));
            goodIsChoose = ((ImageView) itemView.findViewById(R.id.good_ischoose));
            listView = ((RecyclerView) itemView.findViewById(R.id.goods_listview));
        }
    }
}