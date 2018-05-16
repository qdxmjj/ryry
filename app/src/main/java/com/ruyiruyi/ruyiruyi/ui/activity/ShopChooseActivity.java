package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.fragment.MerchantFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;

public class ShopChooseActivity extends FragmentActivity implements MerchantFragment.OnMerchantViewClick{

    private static final String TAG = ShopChooseActivity.class.getSimpleName();
    private int shopType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_choose);
        Intent intent = getIntent();
        if (intent!=null){
            shopType = intent.getIntExtra(MerchantFragment.SHOP_TYPE,0);
        }


        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        MerchantFragment merchantFragment = new MerchantFragment();
        merchantFragment.setListener(this);
        Bundle bundleMerchant = new Bundle();
        bundleMerchant.putInt(MerchantFragment.SHOP_TYPE,shopType);
        merchantFragment.setArguments(bundleMerchant);
        getSupportFragmentManager().beginTransaction().replace(R.id.my_fragment, merchantFragment).commitAllowingStateLoss();

    }

    /**
     * 返回按钮的点击回调
     */
    @Override
    public void onBackViewClickListener() {
        Log.e(TAG, "onBackViewClickListener: --2--");
        onBackPressed();
    }

    /**
     * shopItem的点击回调
     */
    @Override
    public void onShopItemClickListener(Shop shop) {
        Log.e(TAG, "onShopItemClickListener: ----111----");
        Intent intent = new Intent();
        //intent.getIntExtra("SHOPID",1);
        Bundle bundle = new Bundle();
        bundle.putSerializable("shop",shop);
        intent.putExtras(bundle);
        setResult(TireChangeActivity.CHOOSE_SHOP,intent);
        finish();
    }
}
