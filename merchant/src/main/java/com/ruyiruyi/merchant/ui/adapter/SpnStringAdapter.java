package com.ruyiruyi.merchant.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;

import java.util.ArrayList;
import java.util.List;

public class SpnStringAdapter extends BaseAdapter {
    List<String> datas = new ArrayList<>();
    Context context;

    public SpnStringAdapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void updataAndSetData(List<String> list){
        if (!list.isEmpty()) {
            datas.clear();
            datas.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return  datas==null?null:datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            hodler=new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_item_category,null);
            hodler.mTextView = (TextView) convertView.findViewById(R.id.tv_spnitem_category);
            convertView.setTag(hodler);
        }else {
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.mTextView.setText(datas.get(position));
        return convertView;
    }
    private static class ViewHodler{
        TextView mTextView;

    }
}