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

public class MyInviteAwardViewBinder extends ItemViewProvider<MyInviteAward, MyInviteAwardViewBinder.ViewHolder> {
    private Context context;

    public MyInviteAwardViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_invite_award, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MyInviteAward myInviteAward) {

        if ("精致洗车券".equals(myInviteAward.getTitleStr())) {
            holder.civ_person.setImageResource(R.drawable.ic_xiche);
        } else {
            holder.civ_person.setImageResource(R.drawable.ic_hongbao);

        }
        holder.title.setText(myInviteAward.getTitleStr());
        holder.type.setText(myInviteAward.getTypeStr());
        String times = new UtilsRY().getTimestampToStringAll(myInviteAward.getTime());
        holder.time.setText(times);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView civ_person;
        private final TextView title;
        private final TextView time;
        private final TextView type;

        ViewHolder(View itemView) {
            super(itemView);
            civ_person = (CircleImageView) itemView.findViewById(R.id.civ_person);
            title = ((TextView) itemView.findViewById(R.id.title));
            time = ((TextView) itemView.findViewById(R.id.time));
            type = ((TextView) itemView.findViewById(R.id.type));
        }
    }

}