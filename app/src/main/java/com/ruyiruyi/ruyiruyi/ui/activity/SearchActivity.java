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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Log.e(TAG, "SearchAciton: " + string);
                Intent intent = new Intent();
                intent.putExtra("SEARCH_STR",string);
                setResult(MerchantFragment.SEARCH_CODE,intent);
                finish();
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
                Intent intent = new Intent();
                intent.putExtra("SEARCH_STR",string);
                setResult(MerchantFragment.SEARCH_CODE,intent);
                finish();
            }
        });
    }
}
