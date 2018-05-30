package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.fragment.MerchantFragment;
import com.ruyiruyi.rylibrary.android.searchview.ICallBack;
import com.ruyiruyi.rylibrary.android.searchview.ItemCallBack;
import com.ruyiruyi.rylibrary.android.searchview.SearchView;
import com.ruyiruyi.rylibrary.android.searchview.bCallBack;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private SearchView searchView;
    public static String TYPE = "TYPE";
    private int type = 0;  //0是搜索稳点  1是搜索商品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE,0);

        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Log.e(TAG, "SearchAciton: " + string);
                if (type == 0){
                    Intent intent = new Intent();
                    intent.putExtra("SEARCH_STR",string);
                    setResult(MerchantFragment.SEARCH_CODE,intent);
                    finish();
                }else if (type == 1){
                    Intent intent = new Intent(getApplicationContext(),GoodsShopActivity.class);
                    intent.putExtra("SEARCH_STR",string);
                    intent.putExtra(GoodsShopActivity.FROMTYPE,1);
                    startActivity(intent);
                }

            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });

        searchView.setOnItemClickBack(new ItemCallBack() {
            @Override
            public void ItemClick(String string) {
                Log.e(TAG, "SearchAciton: " + string);

                if (type == 0){
                    Intent intent = new Intent();
                    intent.putExtra("SEARCH_STR",string);
                    setResult(MerchantFragment.SEARCH_CODE,intent);
                    finish();
                }else if (type == 1){
                    Intent intent = new Intent(getApplicationContext(),GoodsShopActivity.class);
                    intent.putExtra("SEARCH_STR",string);
                    intent.putExtra(GoodsShopActivity.FROMTYPE,1);
                    startActivity(intent);
                }
            }
        });
    }
}
