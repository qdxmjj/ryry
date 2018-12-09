package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.Util;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import rx.functions.Action1;

public class NewQrCodeActivity extends RyBaseActivity {
    private TextView tv_code;
    private ImageView iv_code;
    private TextView tv_saveimg;
    private ProgressDialog startDialog;
    private LinearLayout ll_main;
    private LinearLayout ll_code;
    private ActionBar actionBar;

    private String TAG = NewQrCodeActivity.class.getSimpleName();
    private String url;
    private String invitationCode;
    private String shareDescription;
    private Bitmap logo;
    private static final int IMAGE_HALFWIDTH = 18;//宽度值，影响中间图片大小

    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_qr_code);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("我的二维码");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        startDialog = new ProgressDialog(this);
        ll_main = findViewById(R.id.ll_main);
        ll_code = findViewById(R.id.ll_code);
        ll_main.setVisibility(View.INVISIBLE);
        showDialogProgress(startDialog, "二维码信息加载中...");

        initView();
        initData();
        bindView();
    }

    private void bindView() {
        //二维码长按
        iv_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //show底部sheet
                showBottomShareMenu();
                return true;
            }
        });
        //保存图片按钮
        RxViewAction.clickNoDouble(tv_saveimg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //设置缓存
                ll_code.setDrawingCacheEnabled(true);
                ll_code.buildDrawingCache();
                //保存控件截图
                Bitmap drawingCache = ll_code.getDrawingCache();

                try {
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + "如驿如意邀请码" + new DbConfig(NewQrCodeActivity.this).getUser().getNick() + ".png");
                    FileOutputStream out = new FileOutputStream(file);
                    drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Toast.makeText(NewQrCodeActivity.this, "二维码图片已保存至手机" + file, Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 显示底部分享菜单栏
     */
    private void showBottomShareMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NewQrCodeActivity.this);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(NewQrCodeActivity.this).inflate(R.layout.bottomsheet_share, null, false);
        LinearLayout ll_share_weixin = contentView.findViewById(R.id.ll_share_weixin);
        RxViewAction.clickNoDouble(ll_share_weixin).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //微信分享给好友
                mTargetScene = SendMessageToWX.Req.WXSceneSession;
                shareToWexin();
            }
        });
        LinearLayout ll_share_weixin_friend = contentView.findViewById(R.id.ll_share_weixin_friend);
        RxViewAction.clickNoDouble(ll_share_weixin_friend).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //微信分享至朋友圈
                mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                shareToWexin();
            }
        });
        bottomSheetDialog.setContentView(contentView);
        //设置底部白色背景为透明
        bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(NewQrCodeActivity.this.getResources().getColor(R.color.transparent));
        bottomSheetDialog.show();
    }

    /**
     * 微信分享
     */
    private void shareToWexin() {
        int id = new DbConfig(this).getId();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "如驿如意";
        msg.description = shareDescription;//分享活动介绍
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void initView() {
        tv_code = findViewById(R.id.tv_code);
        iv_code = findViewById(R.id.iv_code);
        tv_saveimg = findViewById(R.id.tv_saveimg);
    }

    private void initData() {
        //下载数据
        JSONObject object = new JSONObject();
        try {
            object.put("userId", new DbConfig(this).getId());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "preferentialInfo/queryShareInfo");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(this).getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:  result789 = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        url = data.getString("url");
                        invitationCode = data.getString("invitationCode");
                        shareDescription = data.getString("title");

                        //下载完成更新数据
                        updataData();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void updataData() {
        tv_code.setText(invitationCode);
        //将url转成二维码填至iv_code中
        logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        try {
            //调用方法createCode生成二维码
            Bitmap qrBitmap = createCode(url, logo, BarcodeFormat.QR_CODE);
            //将二维码在界面中显示
            iv_code.setImageBitmap(qrBitmap);

            //最后隐藏动画，显示数据页面
            ll_main.setVisibility(View.VISIBLE);
            hideDialogProgress(startDialog);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成二维码
     *
     * @param string  二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     *                [url=home.php?mod=space&uid=309376]@return[/url] Bitmap 位图
     * @throws WriterException
     */
    public Bitmap createCode(String string, Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 300, 300, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
                    //记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
