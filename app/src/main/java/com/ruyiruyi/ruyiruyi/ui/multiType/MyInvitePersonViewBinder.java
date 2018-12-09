package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.utils.CircleImageView;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;

import me.drakeet.multitype.ItemViewProvider;

public class MyInvitePersonViewBinder extends ItemViewProvider<MyInvitePerson, MyInvitePersonViewBinder.ViewHolder> {
    private Context context;

    public MyInvitePersonViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_invite_persion, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyInvitePerson myInvitePerson) {

        holder.civ_person.setImageResource(R.drawable.ic_touxiang);

        String phone = myInvitePerson.getPhone();
        String phoneStr = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        holder.phone.setText(phoneStr);
        String times = new UtilsRY().getTimestampToStringAll(myInvitePerson.getTime());
        holder.time.setText(times);
        switch (myInvitePerson.getStatus()) {
            case 1:
                holder.status.setText("已邀请");
                break;
            case 2:
                holder.status.setText("已注册App");
                break;
            case 3:
                holder.status.setText("已注册车辆信息");
                break;
            case 4:
                holder.status.setText("已购买并安装轮胎");
                break;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView civ_person;
        private final TextView phone;
        private final TextView time;
        private final TextView status;

        ViewHolder(View itemView) {
            super(itemView);
            civ_person = (CircleImageView) itemView.findViewById(R.id.civ_person);
            phone = ((TextView) itemView.findViewById(R.id.phone));
            time = ((TextView) itemView.findViewById(R.id.time));
            status = ((TextView) itemView.findViewById(R.id.status));
        }
    }

}