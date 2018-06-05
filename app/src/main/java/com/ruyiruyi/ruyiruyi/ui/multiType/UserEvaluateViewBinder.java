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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.utils.FullyLinearLayoutManager;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class UserEvaluateViewBinder extends ItemViewProvider<UserEvaluate, UserEvaluateViewBinder.ViewHolder> {
    public Context context;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public EvaImageViewBinder.OnEvaluateImageClick listener;

    public void setListener(EvaImageViewBinder.OnEvaluateImageClick listener) {
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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull UserEvaluate userEvaluate) {
        Glide.with(context).load(userEvaluate.getUserImage()).transform(new GlideCircleTransform(context)).into(holder.userImageView);
        holder.usernameText.setText(userEvaluate.getUsetName());
        holder.evaluateTimeText.setText(userEvaluate.getEvaluateTime());
        holder.evaluateContentText.setText(userEvaluate.getEvaluateContent());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
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
        }


    }
    private void initData(List<String> evaluateImageList,int evaluateId) {
        items.clear();
        for (int i = 0; i < evaluateImageList.size(); i++) {
            items.add(new EvaImage(evaluateId,evaluateImageList.get(i)));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();


    }

    private void register() {
        EvaImageViewBinder provider = new EvaImageViewBinder(context);
        provider.setListener(listener);
        adapter.register(EvaImage.class, provider);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView listView;
        private final TextView evaluateContentText;
        private final TextView usernameText;
        private final TextView evaluateTimeText;
        private final ImageView userImageView;

        ViewHolder(View itemView) {
            super(itemView);
            listView = ((RecyclerView) itemView.findViewById(R.id.phote_imageview));
            evaluateContentText = ((TextView) itemView.findViewById(R.id.evaluate_content_text));
            usernameText = ((TextView) itemView.findViewById(R.id.username_text));
            evaluateTimeText = ((TextView) itemView.findViewById(R.id.evaluate_time_text));
            userImageView = ((ImageView) itemView.findViewById(R.id.user_image));

        }
    }

}