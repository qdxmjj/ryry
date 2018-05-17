package com.ruyiruyi.ruyiruyi.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.fragment.MyFragment;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.FormatDateUtil;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.functions.Action1;

public class UserInfoActivity extends BaseActivity implements DatePicker.OnDateChangedListener {

    private ActionBar mActionBar;
    private LinearLayout ll_change;
    private LinearLayout ll_sex;
    private LinearLayout ll_email;
    private LinearLayout ll_birth;
    private ImageView change_img;
    private EditText user_name;
    private TextView user_sex;
    private TextView user_email;
    private TextView user_birth;
    private TextView tv_save_;

    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Uri tempUri;
    private Bitmap imgBitmap;
    private String headimgurl;
    private String nick;
    private int gender;  //1 男  2 女
    private String email;
    private String birthday;
    private String[] sexArry = new String[]{"男", "女"};
    private Boolean isNewPic = false;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private String phone;
    private String remark;
    private String img_Path;
    private FormatDateUtil formatUtil;
    private String TAG = UserInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_user_info);
        mActionBar = (ActionBar) findViewById(R.id.acbar_info);
        mActionBar.setTitle("个人信息");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        date = new StringBuffer();
        formatUtil = new FormatDateUtil();

        initView();
        initData();
        initDateTime();
        bindView();


    }

    private void initData() {
        //get
        User user = new DbConfig().getUser();
        headimgurl = user.getHeadimgurl();
        nick = user.getNick();
        gender = user.getGender(); //1 男 2 女
        email = user.getEmail();
        birthday = user.getBirthday();
        phone = user.getPhone();
        remark = user.getRemark();

        //set
        user_name.setText(nick);
        user_sex.setText(gender == 1 ? "男" : "女");
        user_email.setText(email);
        user_birth.setText(birthday);
        Glide.with(this).load(headimgurl)
                .transform(new GlideCircleTransform(UserInfoActivity.this))
                .into(change_img);
    }

    private void bindView() {
        RxViewAction.clickNoDouble(ll_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(user_sex).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showSexDialog();
            }
        });
        RxViewAction.clickNoDouble(user_email).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showEmailDialog();
            }
        });
        RxViewAction.clickNoDouble(user_birth).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showBirthDialog();
            }
        });
        RxViewAction.clickNoDouble(tv_save_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //条件判断操作
                if (user_name.getText() == null || user_name.getText().length() == 0) {
                    showDialog("昵称不能为空");
                    return;
                }
                nick = user_name.getText().toString();

                //判断通过
                showSaveDialog("确定保存修改吗");
            }
        });
    }


    /**
     * 日期选择控件
     */
    private void showBirthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                user_birth.setText(formatUtil.formatDateAll(year, month + 1, day)); //此控件月为 0-11；
                birthday = user_birth.getText().toString();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, this);
    }

    // DatePicker控件监听  需实现 DatePicker.OnDateChangedListener 接口
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void showEmailDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_single_input, null);
        final EditText et_input = view.findViewById(R.id.et_input);
        et_input.setText(email);
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (et_input.getText() == null || et_input.getText().length() == 0) {
                    Toast.makeText(UserInfoActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    email = et_input.getText().toString();
                    user_email.setText(email);
                    dialog.dismiss();
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void showSexDialog() {
        String sexStr = user_sex.getText().toString();
        int sexIndex;
        if (sexStr.equals("男")) {
            sexIndex = 0;
        } else {
            sexIndex = 1;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框

        builder.setSingleChoiceItems(sexArry, sexIndex, new DialogInterface.OnClickListener() {// 初设的性别默认选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                user_sex.setText(sexArry[which]);
                gender = which + 1;//下标：0 男 1 女  ；gender 1 男 2 女 ；
                dialog.dismiss();// 点击item对话框消失
            }
        });
        builder.show();// 让弹出框显示
    }

    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("上传照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://选择本地照片
                        Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openBendiPicIntent.setType("image/*");
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(UserInfoActivity.this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = new File(Environment
                .getExternalStorageDirectory(), "userinfoimg.jpg");

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.ruyiruyi.ruyiruyi.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri);
                    break;
                case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri);
                    break;

            }
        }
    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri) {
        int degree = ImageUtils.readPictureDegree(uri.toString());
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(UserInfoActivity.this, uri);
            } catch (IOException e) {
            }
            imgBitmap = rotaingImageView(degree, photo);
//   2          mGoodsImg.setImageBitmap(imgBitmap);
            //Glide 加载BitMap需要先将bitmap对象转换为字节,在加载;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(this).load(bytes)
                    .transform(new GlideCircleTransform(UserInfoActivity.this))
                    .into(change_img);

            //此时记录头像已更改 并生成文件地址
            isNewPic = true;
            img_Path = ImageUtils.savePhoto(imgBitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "userheadimg");
        }
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }


    private void initView() {
        ll_change = findViewById(R.id.ll_change);
        ll_sex = findViewById(R.id.ll_sex);
        ll_email = findViewById(R.id.ll_email);
        ll_birth = findViewById(R.id.ll_birth);
        change_img = findViewById(R.id.change_img);
        user_name = findViewById(R.id.user_name);
        user_sex = findViewById(R.id.user_sex);
        user_email = findViewById(R.id.user_email);
        user_birth = findViewById(R.id.user_birth);
        tv_save_ = findViewById(R.id.tv_save_);

    }


    private void showDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void showSaveDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //确认提交 请求提交数据
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject object = new JSONObject();
                try {
                    object.put("userId", new DbConfig().getId());
                    object.put("id", new DbConfig().getId());
                    object.put("phone", phone);
                    object.put("remark", remark);
                    object.put("age", 0);//age 已去除  传0
                    object.put("nick", nick);
                    object.put("gender", gender);
                    object.put("email", email);
                    object.put("birthday", birthday);
                    object.put("headimgurl", headimgurl);

                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "updateUser");
                params.addBodyParameter("reqJson", object.toString());
                params.addBodyParameter("token", new DbConfig().getToken());
                if (isNewPic) {
                    params.addBodyParameter("user_head_img", new File(img_Path));
                }
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject objects = new JSONObject(result);
                            String msg = objects.getString("msg");
                            int status = objects.getInt("status");
                            if (status == 1) {
                                Log.e(TAG, "onSuccess: msg1 = " + msg);
                                User user = new DbConfig().getUser();
                                user.setNick(nick);
                                user.setGender(gender);
                                user.setEmail(email);
                                user.setBirthday(birthday);
                                saveUserToDb(user);//保存更新到数据库
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //修改成功
                                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                                intent.putExtra(MyFragment.FROM_FRAGMENT, "MYFRAGMENT");
                                startActivity(intent);
                            } else {
                                Log.e(TAG, "onSuccess: msg2 = " + msg);
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
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }

    private void saveUserToDb(User user) {
        DbConfig dbConfig = new DbConfig();
        DbManager.DaoConfig daoConfig = dbConfig.getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        List<User> data = new ArrayList<>();
        data.add(user);
        try {
            db.saveOrUpdate(data);
        } catch (DbException e) {

        }
    }
}
