package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruyiruyi.ruyiruyi.R;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


/**
 * Created by Lenovo on 2018/9/30.
 */
public class HotShopListViewBinder extends ItemViewProvider<HotShopList, HotShopListViewBinder.ViewHolder> {
    public Context context;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    public HotShopViewBinder.OnHotShopItemClick listener;

    public void setListener(HotShopViewBinder.OnHotShopItemClick listener) {
        this.listener = listener;
    }

    public HotShopListViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_hot_shop_list, parent, false);
        return new ViewHolder(root);


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull HotShopList hotShopList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);

        initData(hotShopList.getHotShopList());


    }

    private void initData(List<HotShop> hotShopList) {
        items.clear();
        for (int i = 0; i < hotShopList.size(); i++) {
            items.add(hotShopList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void register() {
        HotShopViewBinder hotShopViewBinder = new HotShopViewBinder(context);
        hotShopViewBinder.setListence(listener);
        adapter.register(HotShop.class, hotShopViewBinder);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView listView;

        ViewHolder(View itemView) {
            super(itemView);
            listView = ((RecyclerView) itemView.findViewById(R.id.hot_shop_listview));
        }
    }
}
