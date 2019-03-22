package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.CarBrand;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.CarModel;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

public class CarBrandActivity extends RyBaseActivity {
    private static final String TAG = CarInfoActivity.class.getSimpleName();
    private ActionBar actionBar;
    private IndexableLayout indexableLayout;
    private CarAdapter mAdapter;
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("车型选择");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        from = intent.getIntExtra("FROM",0);


        initView();
        initAdapter();
        onlisten();
    }

    public void onlisten(){

        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CarModel>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CarModel entity) {
                Intent intent = new Intent(getApplicationContext(),CarDemioActivity.class);
                intent.putExtra("CARID",entity.getCarId());
                intent.putExtra("FROM",from);
                startActivity(intent);
               /* Log.e(TAG, "onItemClick: " + entity.getCarIcon());
                Log.e(TAG, "onItemClick: " + entity.getCarName());
                Log.e(TAG, "onItemClick: " + entity.getCarId());*/
            }


        });
    }

    private void initAdapter() {
        mAdapter = new CarAdapter(this);
        indexableLayout.setAdapter(mAdapter);
        indexableLayout.setOverlayStyle_Center();
        mAdapter.setDatas(initDatas());
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
    //    Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(this)));

    }

    private List<CarModel> initDatas() {
        List<CarModel> list = new ArrayList<>();
        // 初始化数据
      /*  List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        for (int i = 0; i < contactStrings.size(); i++) {
            UserEntity contactEntity = new UserEntity(contactStrings.get(i), mobileStrings.get(i));
            list.add(contactEntity);
        }*/
        DbManager db = new DbConfig(this).getDbManager();
        try {
            List<CarBrand> brandList = db.selector(CarBrand.class).findAll();
            for (int i = 0; i < brandList.size(); i++) {
                CarBrand carBrand = brandList.get(i);
                list.add(new CarModel(carBrand.getId(),carBrand.getName(),carBrand.getImageUrl(),carBrand.getIcon()));
            }

        } catch (DbException e) {

        }
        return list;
    }

    private void initView() {
        indexableLayout = (IndexableLayout) findViewById(R.id.indexableLayout);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
    }

    public class CarAdapter extends IndexableAdapter<CarModel>{

        private LayoutInflater mInflater;

        public CarAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }
        //设置悬浮块的layout
        @Override
        public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
            View view = mInflater.inflate(com.ruyiruyi.rylibrary.R.layout.item_index_contact, parent, false);
            return new IndexVH(view);
        }
        //设置悬浮块下面的layout
        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_car_brand, parent, false);
            return new ContentVH(view);
        }
        //设置悬浮块的数据
        @Override
        public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
            IndexVH vh = (IndexVH) holder;
            vh.tv.setText(indexTitle);
        }
        //设置悬浮块下面的数据
        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, CarModel entity) {
            ContentVH vh = (ContentVH) holder;
            Glide.with(getApplicationContext()).load(entity.getCarIcon()).into(vh.carImage);
            vh.carName.setText(entity.getCarName());
        }

        private class IndexVH extends RecyclerView.ViewHolder {
            TextView tv;

            public IndexVH(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(com.ruyiruyi.rylibrary.R.id.tv_index);
            }
        }

        private class ContentVH extends RecyclerView.ViewHolder {
            ImageView carImage;
            TextView carName;


            public ContentVH(View itemView) {
                super(itemView);
                carImage = (ImageView) itemView.findViewById(R.id.car_image);
                carName = (TextView) itemView.findViewById(R.id.car_name);
            }
        }
    }

}
