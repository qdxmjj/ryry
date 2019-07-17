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
import android.graphics.Color;
import android.widget.Toast;

import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;

import java.util.Calendar;
import java.util.TimeZone;

public class Signature {
    private Context mContext = null;
    private String mDocPath = null;
    private String mCertPath = null;
    private String mCertPassword = null;

    public Signature(Context context, String docPath, String certPath, String certPassword) {
        mContext = context;
        mDocPath = docPath;
        mCertPath = certPath;
        mCertPassword = certPassword;
    }

    public void addSignature(int pageIndex) {
        int indexPdf = mDocPath.lastIndexOf(".");
        int indexSep = mDocPath.lastIndexOf("/");
        String filenameWithoutPdf = mDocPath.substring(indexSep + 1, indexPdf);
        String outputFilePath = Common.getOutputFilesFolder(Common.signatureModuleName) + filenameWithoutPdf + "_add.pdf";
        PDFDoc doc = Common.loadPDFDoc(mContext, mDocPath, null);
        if (doc == null) return;

        try {
            String filter= "Adobe.PPKLite";
            String subfilter= "adbe.pkcs7.detached";
            String dn= "dn";
            String location= "location";
            String reason= "reason";
            String contactInfo= "contactInfo";
            String signer= "signer";
            String text= "text";
            long state = 0;
            String value = null;
            RectF rect = new RectF(100,100,300,300);

            //set current time to dateTime.
            DateTime dateTime = new DateTime();
            Calendar c = Calendar.getInstance();
            TimeZone timeZone = c.getTimeZone();
            int offset = timeZone.getRawOffset();
            int tzHour = offset/(3600*1000);
            int tzMinute = (offset / (1000 * 60)) % 60;
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DATE);
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);
            dateTime.set(year, month, day, hour, minute, second, 0, (short)tzHour,tzMinute);

            int pageCount = doc.getPageCount();
            if (pageIndex > pageCount || pageIndex < 0) {
                Toast.makeText(mContext, String.format("The page index is out of range!"), Toast.LENGTH_LONG).show();
                return;
            }

            PDFPage pdfPage = Common.loadPage(mContext, doc, pageIndex, PDFPage.e_ParsePageNormal);
            if (pdfPage == null || pdfPage.isEmpty()) {
                return;
            }
            com.foxit.sdk.pdf.Signature signature = pdfPage.addSignature(rect);
            signature.setFilter(filter);
            signature.setSubFilter(subfilter);

            signature.setKeyValue(com.foxit.sdk.pdf.Signature.e_KeyNameDN, dn);
            signature.setKeyValue(com.foxit.sdk.pdf.Signature.e_KeyNameLocation, location);
            signature.setKeyValue(com.foxit.sdk.pdf.Signature.e_KeyNameReason, reason);
            signature.setKeyValue(com.foxit.sdk.pdf.Signature.e_KeyNameContactInfo, contactInfo);
            signature.setKeyValue(com.foxit.sdk.pdf.Signature.e_KeyNameSigner, signer);
            signature.setKeyValue(com.foxit.sdk.pdf.Signature.e_KeyNameText, text);
            signature.setSignTime(dateTime);
            int flags = com.foxit.sdk.pdf.Signature.e_APFlagSigningTime | com.foxit.sdk.pdf.Signature.e_APFlagFoxitFlag |
                    com.foxit.sdk.pdf.Signature.e_APFlagLocation | com.foxit.sdk.pdf.Signature.e_APFlagBitmap |
                    com.foxit.sdk.pdf.Signature.e_APFlagReason | com.foxit.sdk.pdf.Signature.e_APFlagSigner |
                    com.foxit.sdk.pdf.Signature.e_APFlagText | com.foxit.sdk.pdf.Signature.e_APFlagDN
                    |com.foxit.sdk.pdf.Signature.e_APFlagLabel;

            Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.BLUE);
            signature.setBitmap(bitmap);

            signature.setAppearanceFlags(flags);

            Progressive progressive = signature.startSign(mCertPath, mCertPassword.getBytes(), com.foxit.sdk.pdf.Signature.e_DigestSHA1, outputFilePath,null, null);
            int progress = Progressive.e_ToBeContinued;
            while (progress == Progressive.e_ToBeContinued) {
                progress = progressive.resume();
            }

            state = signature.getState();
            if (state != com.foxit.sdk.pdf.Signature.e_StateSigned || !signature.isSigned()){
                Toast.makeText(mContext, String.format("This document sign failed !!!"), Toast.LENGTH_LONG).show();
                return;
            }

            PDFDoc signedDoc = Common.loadPDFDoc(mContext, outputFilePath, null);
            int count = signedDoc.getSignatureCount();
            if (count <= 0)
                return;
            signature = signedDoc.getSignature(0);

            if (!signature.isSigned()){
                Toast.makeText(mContext, String.format("This document isn`t signed !!!"), Toast.LENGTH_LONG).show();
                return;
            }

            progressive = signature.startVerify(null, null);
            progress = Progressive.e_ToBeContinued;
            while (progress == Progressive.e_ToBeContinued) {
                progress = progressive.resume();
            }

            state = signature.getState();
            if ((state & com.foxit.sdk.pdf.Signature.e_StateVerifyValid) != com.foxit.sdk.pdf.Signature.e_StateVerifyValid){
                Toast.makeText(mContext, String.format("This document verify failed !!!"), Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(mContext, Common.runSuccesssInfo + outputFilePath, Toast.LENGTH_LONG).show();
        } catch (PDFException e) {
            Toast.makeText(mContext, String.format("Failed to sign the page No.%d! %s", pageIndex, e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }
}
