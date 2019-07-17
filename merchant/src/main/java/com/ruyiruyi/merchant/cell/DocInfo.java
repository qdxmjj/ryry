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

import com.foxit.sdk.pdf.Metadata;
import com.foxit.sdk.pdf.PDFDoc;

import java.io.File;
import java.io.FileWriter;

public class DocInfo {
    private Context mContext = null;
    private String mPath = null;

    public DocInfo(Context context, String path) {
        mContext = context;
        mPath = path;
    }

    public void outputDocInfo() {
        PDFDoc doc = Common.loadPDFDoc(mContext, mPath, null);
        if (doc == null) {
            return;
        }

        String filenameWithoutPdf = mPath.substring(mPath.lastIndexOf("/") + 1, mPath.lastIndexOf("."));
        String outputFilePath = Common.getOutputFilesFolder(Common.docInfoModuleName) + filenameWithoutPdf + "_docinfo.txt";
        File txtFile = new File(outputFilePath);
        try {
            FileWriter fileWriter = new FileWriter(txtFile);

            //pageCount
            int pageCount = doc.getPageCount();
            fileWriter.write(String.format("Page Count: %d pages\r\n", pageCount));

            //title
            Metadata metadata = new Metadata(doc);

            String title = String.format("Title :%s\r\n", metadata.getValues("Title").getAt(0));
            //If there is no title info in the document, it uses the file name instead.
            if (title == null && title.equals("")) {
                title = filenameWithoutPdf;
            }
            fileWriter.write(title);

            //author
            fileWriter.write(String.format("Author: %s\r\n",  metadata.getValues("Author").getAt(0)));
            //subject
            fileWriter.write(String.format("Subject: %s\r\n", metadata.getValues("Subject").getAt(0)));
            //keywords
            fileWriter.write(String.format("Keywords: %s\r\n", metadata.getValues("Keywords").getAt(0)));

            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Toast.makeText(mContext, String.format("Failed to export doc info of %s!", mPath), Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(mContext, Common.runSuccesssInfo + outputFilePath, Toast.LENGTH_LONG).show();
    }
}
