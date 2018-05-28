package com.ruyiruyi.merchant.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UtilsRY {

    public static boolean isMobile(String number) {
        String num = "[1][34578]\\d{9}";
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

}