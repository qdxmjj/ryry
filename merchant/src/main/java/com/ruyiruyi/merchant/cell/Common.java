/**
 * Copyright (C) 2003-2018, Foxit Software Inc..
 * All Rights Reserved.
 * <p>
 * http://www.foxitsoftware.com
 * <p>
 * The following code is copyrighted and is the proprietary of Foxit Software Inc.. It is not allowed to
 * distribute any parts of Foxit PDF SDK to third party or public without permission unless an agreement
 * is signed between Foxit Software Inc. and customers to explicitly grant customers permissions.
 * Review legal.txt for additional license and legal information.
 */
package com.ruyiruyi.merchant.cell;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.Time;
import android.widget.Toast;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimeZone;
import java.util.UUID;

public class Common {

    public final static String pdf2textModuleName = "pdf2text";
    public final static String outlineModuleName = "outline";
    public final static String docInfoModuleName = "docInfo";
    public final static String renderModuleName = "render";
    public final static String annotationModuleName = "annotation";
    public final static String signatureModuleName = "signature";

    public static final String testInputFile = "FoxitBigPreview.pdf";
    public static final String outlineInputFile = "Outline.pdf";
    public static final String anotationInputFile = "Annotation.pdf";
    public static final String signatureInputFile = "Sample.pdf";
    public static final String signatureCertification = "foxit_all.pfx";

    public static final String runSuccesssInfo = "Successfully! The generated file was saved to ";

    private static String externalPath;

    public static String getExternalPath() {
        return externalPath;
    }

    public static void setExternalPath(String path) {
        externalPath = path;
    }

    public static boolean checkSD() {
        boolean sdExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdExist) {
            File sddir = Environment.getExternalStorageDirectory();
            setExternalPath(sddir.toString());
        } else {
            setExternalPath(null);
        }
        return sdExist;
    }

    public static String getFixFolder() {
        String path = getExternalPath();
//        path += "/input_files/";
        return path;
    }

    public static boolean createFolder(String folderPath) {
        try {
            File myFilePath = new File(folderPath);
            if (!myFilePath.exists()) {
                myFilePath.mkdirs();
            }
        } catch (Exception e) {
        }
        return true;
    }

    public static String getOutputFilesFolder(String moduleName) {
        //Combine the current external path, outputting files path (fixed) and example module name together
        String outputPath = getExternalPath();
        outputPath += "/output_files/";
        outputPath += moduleName + "/";
        createFolder(outputPath);
        return outputPath;
    }

    public static boolean saveImageFile(Bitmap bitmap, Bitmap.CompressFormat picFormat, String fileName) {
        File file = new File(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(picFormat, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static PDFDoc loadPDFDoc(Context context, String path, byte[] password) {
        try {
            PDFDoc doc = new PDFDoc(path);
            if (doc == null) {
                Toast.makeText(context, String.format("The path %s does not exist!", path), Toast.LENGTH_LONG).show();
                return null;
            }

            doc.load(password);
            return doc;
        } catch (PDFException e) {
            Toast.makeText(context, "Load document error. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public static PDFPage loadPage(Context context, PDFDoc doc, int index, int parseFlag) {
        PDFPage page = null;
        if (doc == null) {
            Toast.makeText(context, "The document is null!", Toast.LENGTH_LONG).show();
            return page;
        }

        try {
            page = doc.getPage(index);
            if (page == null) {
                Toast.makeText(context, "Get Page error", Toast.LENGTH_LONG).show();
                return page;
            }

            if (!page.isParsed()) {
                Progressive progressive = page.startParse(parseFlag, null, false);

                int state = Progressive.e_ToBeContinued;
                while (state == Progressive.e_ToBeContinued) {
                    state = progressive.resume();
                }

                if (state == Progressive.e_Error) {
                    Toast.makeText(context, "Parse Page error!", Toast.LENGTH_LONG).show();
                    return null;
                }
            }

        } catch (PDFException e) {
            Toast.makeText(context, "Load Page error. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return page;
    }

    public static DateTime getCurrentDateTime() {
        Time now = new Time();
        now.setToNow();

        DateTime dateTime = null;

        int year = now.year;
        int month = now.month + 1;
        int date = now.monthDay;
        int hour = now.hour;
        int minute = now.minute;
        int second = now.second;
        int timezone = TimeZone.getDefault().getRawOffset();
        int localHour = timezone / 3600000;
        int localMinute = timezone % 3600000 / 60;

        dateTime = new DateTime();
        dateTime.set(year, month, date, hour, minute, second, 0, (short) localHour, localMinute);

        return dateTime;
    }

    public static String randomUUID(String separator) {
        String uuid = UUID.randomUUID().toString();
        if (separator != null) {
            uuid.replace("-", separator);
        }
        return uuid;
    }

    //Check whether the SD is available.
    public static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    private static boolean exist(String path) {
        File file = new File(path);
        return file != null && file.exists();
    }

    private static boolean mergeFiles(Context context, String outDir, String[] files) {
        boolean success = false;
        OutputStream os = null;
        try {

            byte[] buffer = new byte[1 << 13];
            for (String f : files) {
                if (exist(getFixFolder() + f))
                    continue;
                os = new FileOutputStream(outDir + f);
                InputStream is = context.getAssets().open(f);
                int len = is.read(buffer);
                while (len != -1) {
                    os.write(buffer, 0, len);
                    len = is.read(buffer);
                }
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                    success = true;
                }
            } catch (IOException ignore) {
            }
        }
        return success;
    }

    public static void copyTestFiles(Context context) {
        String[] testFiles = {anotationInputFile, testInputFile, outlineInputFile, signatureInputFile, signatureCertification};
        if (Common.isSDAvailable()) {
            File file = new File(getSDPath() + File.separator + "input_files");
            if (!file.exists())
                file.mkdirs();
            mergeFiles(context, getFixFolder(), testFiles);
        }
    }
}

