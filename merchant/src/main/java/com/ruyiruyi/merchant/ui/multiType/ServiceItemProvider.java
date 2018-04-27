package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsBean;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.MyServiceActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class ServiceItemProvider extends ItemViewProvider<ServicesBean, ServiceItemProvider.ViewHolder> {

    private String TAG = ServiceItemProvider.class.getSimpleName();
    private List<String> selectServicesList = new ArrayList<>();
    private String selectServicesListString;


    public OnServiceItemClick listener;

    public void setListener(OnServiceItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_services, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ServicesBean servicesBean) {
        holder.tv_service_item.setText(servicesBean.getServiceInfo());
        Log.w(TAG, "onBindViewHolder: servicesBean.getServiceInfo()" + servicesBean.getServiceInfo());
        holder.img_service_item.setTag(servicesBean);
//        holder.img_service_item.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
        RxViewAction.clickNoDouble(holder.service_layout).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.onServiceItemClickListener(servicesBean.getService_id());
            }
        });
        if (servicesBean.getIsChecked() == 1) {
            holder.img_service_item.setImageResource(R.drawable.ic_check);
        } else {
            holder.img_service_item.setImageResource(R.drawable.ic_notcheck);
        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_service_item;
        private final ImageView img_service_item;
        private final FrameLayout service_layout;

        ViewHolder(View itemView) {
            super(itemView);
            tv_service_item = ((TextView) itemView.findViewById(R.id.tv_service_item));
            img_service_item = (ImageView) itemView.findViewById(R.id.img_service_item);
            service_layout = (FrameLayout) itemView.findViewById(R.id.service_layout);
        }
    }

    public interface OnServiceItemClick {
        void onServiceItemClickListener(int id);
    }
}