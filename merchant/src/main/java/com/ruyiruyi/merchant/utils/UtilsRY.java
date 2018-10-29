package com.ruyiruyi.merchant.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UtilsRY {

    public static String delZero(String number) {
        while (number.length() != 1) {
            if (number.substring(0, 1).equals("0")) {
                number = number.substring(1, number.length());
            } else {
                return number;
            }
        }
        return number;
    }

    /**
     * Double
     *
     * @param number
     * @return
     */
    public static boolean isMobile(String number) {
        String num = "[1][345789]\\d{9}";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }

    /**
     * 身份证号码简单校验
     *
     * @param number
     * @return
     */
    public static boolean isIdNumber(String number) {
        String num = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return number.matches(num);
    }

    /**
     * 正浮点数
     *
     * @param number
     * @return
     */
    public static boolean isFloat(String number) {
        String num = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }

    /**
     * 正整数
     *
     * @param number
     * @return
     */
    public static boolean isInt(String number) {
        String num = "^[1-9]\\d*";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }

    /**
     * 1、除了个位，十位以上不能以0开头
     * 2、小数部分可有可元
     * 3、小数点后可以一位或者二位
     *
     * @param number
     * @return
     */
    public static boolean isRighrNumber(String number) {
        String num = "";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }

    public String getTimestampToString(long time) {
        Timestamp ts = new Timestamp(time);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //方法一:优势在于可以灵活的设置字符串的形式。
        String tsStr = sdf.format(ts);
        return tsStr;
    }

    public String getTimestampToStringAll(long time) {
        Timestamp ts = new Timestamp(time);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //方法一:优势在于可以灵活的设置字符串的形式。
        String tsStr = sdf.format(ts);
        return tsStr;
    }

    public Timestamp getStringToTimestamp(String time) {
        Timestamp ts = Timestamp.valueOf(time);

        return ts;

    }

    /**
     * Try to return the absolute file path from the given Uri
     * (Uri 转 Path)
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /*
    * 删除图片文件
    * */
    public static void deleteUri(Context context, Uri uri) {

        if (uri.toString().startsWith("content://")) {
            // content://开头的Uri
            context.getContentResolver().delete(uri, null, null);
        } else {
            File file = new File(getRealFilePath(context, uri));
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * (Path 转 Uri)  有bug  未用
     */
    public static Uri getUri(String path, Context context) {
        Uri uri = null;
        if (path != null) {
            path = Uri.decode(path);
            ContentResolver cr = context.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(")
                    .append(MediaStore.Images.ImageColumns.DATA)
                    .append("=")
                    .append("'" + path + "'")
                    .append(")");
            Cursor cur = cr.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns._ID},
                    buff.toString(), null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur
                    .moveToNext()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                // set _id value
                index = cur.getInt(index);
            }
            if (index == 0) {
                Log.e("do nothing", "getUri: ");
                //do nothing
            } else {
                Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                if (uri_temp != null) {
                    uri = uri_temp;
                    Log.e("getUri", "uri =  " + uri);
                }
            }
        }
        return uri;
    }


    /*
    * 根据String Path 删除图片
    * */
    public static void deleteImage(String imgPath, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = context.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }

        if (result) {
         /*   imageList.remove(imgPath);
            adapter.notifyDataSetChanged();
            Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();*/
            Log.e("UtilsRY ", "DeleteImage: 图片删除成功");
        } else {
            Log.e("UtilsRY ", "DeleteImage: 图片删除失败");
        }
    }

}