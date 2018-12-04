package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.Util;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseWebActivity;
import com.ruyiruyi.rylibrary.utils.RyLoadingDialog;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.functions.Action1;

public class BottomEventActivity extends BaseWebActivity {
    private WebView activity_web;
    private String webUrl;
    private String shareDescription;
    private boolean isBottomEvent;
    private boolean canShare;
    private RyLoadingDialog dialog;
    private String TAG = BottomEventActivity.class.getSimpleName();

    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_event);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        Intent intent = getIntent();
        webUrl = intent.getStringExtra("webUrl");
        shareDescription = intent.getStringExtra("shareDescription");
        isBottomEvent = intent.getBooleanExtra("isBottomEvent", false);
        canShare = intent.getBooleanExtra("canShare", false);
        Log.e(TAG, "onCreate: webUrl = " + webUrl);

        //设置是否显示标题栏
        showTitleBar(true);
        //是否显示左侧文字
        showBackwardView(R.string.web_title, true);
        //设置标题栏背景色
        setTitleBgColor(R.color.web_top);
        //设置状态栏颜色
        setStatus(R.color.web_top);
        //添加左侧图标，
        setLeftIcon(R.drawable.ic_cha);
        //文字颜色
        setTitleColor(R.color.c7);
        //左侧返回按钮监听
        setLeftIconlistener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //判断添加右侧分享菜单
        if (canShare) {
            setRightIcon(R.drawable.share);
            setRightIconlistener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //share
                    showBottomShareMenu();
                }
            });
        }


        initView();
        bindView();
        initData();
        setData();
    }

    /**
     * 显示底部分享菜单栏
     */
    private void showBottomShareMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BottomEventActivity.this);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(BottomEventActivity.this).inflate(R.layout.bottomsheet_share, null, false);
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
        bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(BottomEventActivity.this.getResources().getColor(R.color.transparent));
        bottomSheetDialog.show();
    }

    /**
     * 微信分享
     */
    private void shareToWexin() {
        int id = new DbConfig(this).getId();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
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


    @Override
    public void onForward(View forwardView) {
    }

    private void setData() {
        if (isBottomEvent) {//从主页活动页面进入
            User user = new DbConfig(this).getUser();
            int id = user.getId();
            int carId = user.getCarId();
            activity_web.loadUrl(webUrl + "?userId=" + id + "&userCarId=" + carId);
        } else {//其他入口进入
            activity_web.loadUrl(webUrl);
        }
        activity_web.addJavascriptInterface(this, "android");
        activity_web.setWebViewClient(new SafeWebViewClient());
        activity_web.setWebChromeClient(new SafeWebChromeClient());

        WebSettings settings = activity_web.getSettings();
        if (settings == null) {
            return;
        }

        //支持js使用
        settings.setJavaScriptEnabled(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        //设置WebView缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //支持启用缓存模式
        settings.setAppCacheEnabled(true);
        //保存密码提醒
        settings.setSavePassword(true);
        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);//不显示缩放按钮

    }

    private void initData() {

    }

    private void bindView() {

    }

    private void initView() {
        activity_web = findViewById(R.id.activity_web);

        dialog = new RyLoadingDialog(this, "");
    }


    /**
     * JS调用android的方法
     * <p>
     * •getClient html页面的JS可以通过这个方法回调原生APP，这个方法有个注解@JavascriptInterface，这个是必须的，
     * 这个方法有个字符串参数，这个方法跟我们在onCreate中调用addJavascriptInterface传入的name一起使用的。
     * 例如html中想要回调这个方法可以这样写:javascript:android.getClient(“传一个字符串给客户端”);
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        Log.e("WebActivity", "html调用客户端:" + str);
    }

    public class SafeWebViewClient extends WebViewClient {
        /**
         * 当WebView得页面Scale值发生改变时回调
         */
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        /**
         * 是否在 WebView 内加载页面
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /*非http或https的自定义的协议时webView出现ERR_UNKNOWN_URL_SCHEME 异常的解决方案 bingo~*/
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
            }
            view.loadUrl(url);
            return true;
        }

        /**
         * WebView 开始加载页面时回调，一次Frame加载对应一次回调
         *
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            dialog.show();
        }

        /**
         * WebView 完成加载页面时回调，一次Frame加载对应一次回调
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();

        }

        /**
         * WebView 加载页面资源时会回调，每一个资源产生的一次网络加载，除非本地有当前 url 对应有缓存，否则就会加载。
         *
         * @param view WebView
         * @param url  url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * WebView 访问 url 出错
         */
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        /**
         * WebView ssl 访问证书出错，handler.cancel()取消加载，handler.proceed()对然错误也继续加载
         *
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

    }

    public class SafeWebChromeClient extends WebChromeClient {

        /*
        * 输出Web端日志
        * */
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        /**
         * 当前 WebView 加载网页进度
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        /**
         * Js 中调用 alert() 函数，产生的对话框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(BottomEventActivity.this)
                            .setMessage(message)
                            .setPositiveButton("确定", null)
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {// 屏蔽keycode等于84之类的按键
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return true;
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
            // 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            result.confirm();
            return true;
        }

        /**
         * 处理 Js 中的 Confirm 对话框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, final String message, final JsResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(BottomEventActivity.this)
                            .setMessage(message)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.confirm();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.cancel();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    result.cancel();
                                }
                            })
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {// 屏蔽keycode等于84之类的按键
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return true;
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });

            return true;
        }

        /**
         * 处理 JS 中的 Prompt对话框
         *
         * @param view
         * @param url
         * @param message
         * @param defaultValue
         * @param result
         * @return
         */
        @Override
        public boolean onJsPrompt(final WebView view, String url, final String message, final String defaultValue, final JsPromptResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final EditText et = new EditText(view.getContext());
                    et.setSingleLine();
                    et.setText(defaultValue);
                    new AlertDialog.Builder(BottomEventActivity.this)
                            .setMessage(message)
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.confirm(et.getText().toString());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.cancel();
                                }
                            })
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {// 屏蔽keycode等于84之类的按键
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return true;
                                }
                            })
                            .setCancelable(false)
                            .show();

                }
            });

            return true;
        }

        /**
         * 接收web页面的icon
         *
         * @param view
         * @param icon
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /**
         * 接收web页面的 Title
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (activity_web.canGoBack()) {
                Log.e(TAG, "onKey: canGoBack");
                activity_web.goBack();
            } else {
                Log.e(TAG, "onKey: !canGoBack");
                BottomEventActivity.this.finish();
            }
            return true;
        }

        return false;
    }
}
