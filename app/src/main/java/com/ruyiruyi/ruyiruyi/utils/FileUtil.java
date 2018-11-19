package com.ruyiruyi.ruyiruyi.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by Lenovo on 2018/11/12.
 */

public class FileUtil {
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
}
