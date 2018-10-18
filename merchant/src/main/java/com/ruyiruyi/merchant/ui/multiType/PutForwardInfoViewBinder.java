package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.CircleImageView;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/30 9:18
 */
public class PutForwardInfoViewBinder extends ItemViewProvider<PutForwardInfo, PutForwardInfoViewBinder.ViewHolder> {
    private Context mContext;

    public PutForwardInfoViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_putforwardinfo, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PutForwardInfo putForwardInfo) {
        if (putForwardInfo.getPutForwardType() == 1) {//支付宝支付
            holder.civ_putforwardinfo.setImageResource(R.drawable.ic_pay);
        } else if (putForwardInfo.getPutForwardType() == 2) {//微信支付
            holder.civ_putforwardinfo.setImageResource(R.drawable.ic_wechat);
        }

        holder.tv_putforwardinfo_money.setText(putForwardInfo.getPutForwardMoney() + "");

        if (putForwardInfo.getPutForwardStatus() == 1) {
            holder.tv_putforwardinfo_status.setText("提现中");
            holder.tv_putforwardinfo_status.setTextColor(mContext.getResources().getColor(R.color.c7));
        } else if (putForwardInfo.getPutForwardStatus() == 2) {
            holder.tv_putforwardinfo_status.setText("提现成功");
            holder.tv_putforwardinfo_status.setTextColor(mContext.getResources().getColor(R.color.c6));
        } else if (putForwardInfo.getPutForwardStatus() == 3) {
            holder.tv_putforwardinfo_status.setText("提现失败");
            holder.tv_putforwardinfo_status.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
        }

        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(putForwardInfo.getPutForwardTime());
        holder.tv_putforwardinfo_time.setText(timestampToStringAll);

        RxViewAction.clickNoDouble(holder.fl_main).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showInfoDialog(putForwardInfo);
            }
        });

    }

    private void showInfoDialog(PutForwardInfo putForwardInfo) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_putforward_info, null);
        CircleImageView civ_type = (CircleImageView) dialogView.findViewById(R.id.civ_type);
        TextView tv_type = (TextView) dialogView.findViewById(R.id.tv_type);
        TextView tv_money = (TextView) dialogView.findViewById(R.id.tv_money);
        TextView tv_status = (TextView) dialogView.findViewById(R.id.tv_status);
        TextView tv_msg = (TextView) dialogView.findViewById(R.id.tv_msg);
        TextView tv_time = (TextView) dialogView.findViewById(R.id.tv_time);
        TextView tv_no = (TextView) dialogView.findViewById(R.id.tv_no);
        civ_type.setImageResource(putForwardInfo.getPutForwardType() == 1 ? R.drawable.ic_pay : R.drawable.ic_wechat);
        tv_type.setText(putForwardInfo.getPutForwardType() == 1 ? "支付宝提现" : "微信提现");
        tv_money.setText(putForwardInfo.getPutForwardMoney() + "");
        if (putForwardInfo.getPutForwardStatus() == 1) {
            tv_status.setText("提现中");
        } else if (putForwardInfo.getPutForwardStatus() == 2) {
            tv_status.setText("提现成功");
        } else if (putForwardInfo.getPutForwardStatus() == 3) {
            tv_status.setText("提现失败");
            tv_status.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
        }
        tv_msg.setText(putForwardInfo.getRemark());
        /*tv_msg.setText("提现申请提交后预计7个工作日内到账，请耐心等待");*/
        /*tv_msg.setText("提现金额到账成功，请注意查收");*/
        /*tv_msg.setText("微信账号实名信息与提现申请姓名不一致");*/
        String times = new UtilsRY().getTimestampToStringAll(putForwardInfo.getPutForwardTime());
        tv_time.setText(times);
        tv_no.setText(putForwardInfo.getOrderNo());
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView civ_putforwardinfo;
        private final TextView tv_putforwardinfo_money;
        private final TextView tv_putforwardinfo_status;
        private final TextView tv_putforwardinfo_time;
        private final FrameLayout fl_main;

        ViewHolder(View itemView) {
            super(itemView);
            civ_putforwardinfo = itemView.findViewById(R.id.civ_putforwardinfo);
            tv_putforwardinfo_money = itemView.findViewById(R.id.tv_putforwardinfo_money);
            tv_putforwardinfo_status = itemView.findViewById(R.id.tv_putforwardinfo_status);
            tv_putforwardinfo_time = itemView.findViewById(R.id.tv_putforwardinfo_time);
            fl_main = itemView.findViewById(R.id.fl_main);
        }
    }
}
