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
import android.widget.Toast;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Pdf2text {
    private String mFilePath = "";
    private Context mContext = null;

    public Pdf2text(Context context, String pdfFilePath) {
        mFilePath = pdfFilePath;
        mContext = context;
    }

    public void doPdfToText() {
        int indexPdf = mFilePath.lastIndexOf(".");
        int indexSep = mFilePath.lastIndexOf("/");

        String filenameWithoutPdf = mFilePath.substring(indexSep + 1, indexPdf);
        String outputFilePath = Common.getOutputFilesFolder(Common.pdf2textModuleName) + filenameWithoutPdf + ".txt";
        String strText = "";

        PDFDoc doc = Common.loadPDFDoc(mContext, mFilePath, null);
        if (doc == null) {
            return;
        }

        PDFPage page = null;
        try {
            int pageCount = doc.getPageCount();

            //Traverse pages and get the text string.
            for (int i = 0; i < pageCount; i++) {
                page = Common.loadPage(mContext, doc, i, PDFPage.e_ParsePageNormal);
                if (page == null || page.isEmpty()) {
                    continue;
                }

                TextPage textSelect = new TextPage(page, TextPage.e_ParseTextNormal);
                if (textSelect == null || textSelect.isEmpty()) {
                    continue;
                }

                strText += textSelect.getChars(0, textSelect.getCharCount()) + "\r\n";
            }
        } catch (PDFException e) {
            Toast.makeText(mContext, "Pdf to text error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //Output the text string to the TXT file.
        FileWriter fileWriter = null;
        try {
            File fileTxt = new File(outputFilePath);
            fileWriter = new FileWriter(fileTxt);
            fileWriter.write(strText);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(mContext, Common.runSuccesssInfo + outputFilePath, Toast.LENGTH_LONG).show();
    }
}
