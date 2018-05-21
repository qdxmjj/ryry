package com.ruyiruyi.merchant.ui.multiType;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsItemBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.GoodsInfoReeditActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class GoodsItemProvider extends ItemViewProvider<GoodsItemBean, GoodsItemProvider.ViewHolder> {
    private Context context;
    private String TAG = GoodsItemProvider.class.getSimpleName();
    private ProgressDialog progressDialog;

    public GoodsItemProvider(Context context) {
        this.context = context;
    }
    /*    private PassIdToFGListener listener;

    public void setListener(PassIdToFGListener listener) {
        this.listener = listener;
    }*/

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GoodsItemBean goodsItemBean) {
        progressDialog = new ProgressDialog(context);
        holder.tv_title.setText(goodsItemBean.getName());
        holder.tv_kucun.setText(goodsItemBean.getAmount() + "");
        holder.tv_yishou.setText(goodsItemBean.getSoldNo() + "");
        holder.tv_price.setText(goodsItemBean.getPrice() + "");

        String updateTime = String.valueOf(System.currentTimeMillis());
        Glide.with(context).load(goodsItemBean.getImgUrl())//不缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
//                .skipMemoryCache(true)//跳过内存缓存
                .into(holder.img_goods);


        //绑定监听
        RxViewAction.clickNoDouble(holder.tv_delete_goods).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
               /* listener.passtoFG(goodsItemBean.getId() + "");//传递数据*/
                showDeleteDialog(goodsItemBean.getId() + "");
            }
        });
        RxViewAction.clickNoDouble(holder.tv_reedit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
             /*   listener.passtoFG(goodsItemBean.getId() + "");//传递数据*/
                Bundle bundle = new Bundle();
                bundle.putString("goodsid", goodsItemBean.getId() + "");
                bundle.putString("name", goodsItemBean.getName());
                bundle.putString("serviceTypeId", goodsItemBean.getServiceTypeId() + "");
                bundle.putString("serviceId", goodsItemBean.getServiceId() + "");
                bundle.putString("status", goodsItemBean.getStatus() + "");
                bundle.putString("amount", goodsItemBean.getAmount() + "");
                bundle.putString("soldno", goodsItemBean.getSoldNo() + "");
                bundle.putString("price", goodsItemBean.getPrice() + "");
                bundle.putString("imgurl", goodsItemBean.getImgUrl() + "");
                Intent intent = new Intent(context, GoodsInfoReeditActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void showDeleteDialog(final String goodsid) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        error_text.setText("确定删除商品吗");
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_logo_huise);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //确认提交 请求提交数据
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogProgress(progressDialog, "商品删除中...");
                JSONObject object = new JSONObject();
                try {
                    object.put("id", goodsid);
                    object.put("status", "3");//2 下架  1 在售  4 删除
                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "updateStock");
                params.addBodyParameter("reqJson", object.toString());
                params.setConnectTimeout(6000);
                Log.e(TAG, "onClick: 00000 params.toString()  =  " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: 00000  result.toString() =  " + result.toString());
                        try {
                            JSONObject object1 = new JSONObject(result);
                            String status = object1.getString("status");
                            if (status.equals("1")) {
                                Toast.makeText(context, "更新商品成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "更新商品失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(context, "商品删除失败,请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        hideDialogProgress(progressDialog);
                    }
                });
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.theme_primary));

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_title;
        private final TextView tv_kucun;
        private final TextView tv_yishou;
        private final TextView tv_price;
        private final ImageView img_goods;
        private final TextView tv_delete_goods;
        private final TextView tv_reedit;

        ViewHolder(View itemView) {
            super(itemView);
            tv_title = ((TextView) itemView.findViewById(R.id.tv_goodstitle));
            tv_kucun = ((TextView) itemView.findViewById(R.id.tv_goodskucun));
            tv_yishou = ((TextView) itemView.findViewById(R.id.tv_goodsyishou));
            tv_price = ((TextView) itemView.findViewById(R.id.tv_goodsprice));
            img_goods = ((ImageView) itemView.findViewById(R.id.img_goods));
            tv_delete_goods = itemView.findViewById(R.id.tv_delete_goods);
            tv_reedit = itemView.findViewById(R.id.tv_reedit);
        }
    }

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }

    public void hideDialogProgress(ProgressDialog dialog) {
        dialog.dismiss();
    }
}