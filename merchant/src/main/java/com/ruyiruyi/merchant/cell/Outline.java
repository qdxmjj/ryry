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
import com.foxit.sdk.pdf.Bookmark;
import com.foxit.sdk.pdf.PDFDoc;

public class Outline {
    private String mFilePath = "";
    private Context mContext = null;
    private static int index = 0;

    public Outline(Context context, String pdfFilePath) {
        mFilePath = pdfFilePath;
        mContext = context;
    }

    public void modifyOutline() {
        int indexPdf = mFilePath.lastIndexOf(".");
        int indexSep = mFilePath.lastIndexOf("/");

        String filenameWithoutPdf = mFilePath.substring(indexSep + 1, indexPdf);
        String outputFilePath = Common.getOutputFilesFolder(Common.outlineModuleName) + filenameWithoutPdf + "_edit.pdf";

        PDFDoc doc = null;
        doc = Common.loadPDFDoc(mContext, mFilePath, null);
        if (doc == null || doc.isEmpty()) {
            return;
        }
        try {
            Bookmark bookmarkRoot = doc.getRootBookmark();
            if (bookmarkRoot == null) {
                return;
            }

            Bookmark firstChild = bookmarkRoot.getFirstChild();
            modifyOutline(firstChild);

            if (!doc.saveAs(outputFilePath, PDFDoc.e_SaveFlagNormal)) {
                Toast.makeText(mContext, "Save document error!", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (PDFException e) {
            Toast.makeText(mContext, "Outline demo run error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(mContext, Common.runSuccesssInfo + outputFilePath, Toast.LENGTH_LONG).show();
    }

    private void modifyOutline(Bookmark bookmark) {
        try {
            if (bookmark.isEmpty())
                return;

            if (index % 2 == 0) {
                bookmark.setColor(0xFFFF0000);
                bookmark.setStyle(Bookmark.e_StyleBold);
            } else {
                bookmark.setColor(0xFF00FF00);
                bookmark.setStyle(Bookmark.e_StyleItalic);
            }

            bookmark.setTitle("foxitbookmark" + index);
            index++;

            //Traverse the brother nodes and modify their appearance and titles.
            Bookmark nextSibling = bookmark.getNextSibling();
            modifyOutline(nextSibling);

            //Traverse the children nodes and modify their appearance and titles.
            Bookmark child = bookmark.getFirstChild();
            modifyOutline(child);

        } catch (PDFException e) {
            Toast.makeText(mContext, "Outline demo run error. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
