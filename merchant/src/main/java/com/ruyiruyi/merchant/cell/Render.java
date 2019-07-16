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

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.common.Renderer;

public class Render {
    private Context mContext = null;
    private String mPath = null;

    public Render(Context context, String path) {
        mContext = context;
        mPath = path;
    }

    public void renderPage(int index) {
        PDFDoc doc = Common.loadPDFDoc(mContext, mPath, null);
        if (doc == null) return;

        try {
            int pageCount = doc.getPageCount();
            if (index > pageCount || index < 0) {
                Toast.makeText(mContext, String.format("The page index is out of range!"), Toast.LENGTH_LONG).show();
                return;
            }

            String name = mPath.substring(mPath.lastIndexOf("/") + 1, mPath.lastIndexOf("."));
            String outputFilePath = String.format("%s_index_%d.jpg", Common.getOutputFilesFolder(Common.renderModuleName).concat(name), index);
            PDFPage pdfPage = Common.loadPage(mContext, doc, index, PDFPage.e_ParsePageNormal);
            if (pdfPage == null || pdfPage.isEmpty()) {
                return;
            }

            //Create the bitmap and erase its background.
            Bitmap bitmap = Bitmap.createBitmap((int) pdfPage.getWidth(), (int) pdfPage.getHeight(), Bitmap.Config.RGB_565);
            bitmap.eraseColor(Color.WHITE);


            Matrix2D matrix = pdfPage.getDisplayMatrix(0, 0, (int)pdfPage.getWidth(), (int)pdfPage.getHeight(), Constants.e_Rotation0);

            Renderer renderer = new Renderer(bitmap,true);

            //Render the page to bitmap.
            Progressive progressive = renderer.startRender(pdfPage, matrix, null);
            int state = Progressive.e_ToBeContinued;
            while (state == Progressive.e_ToBeContinued) {
                state = progressive.resume();
            }

            if (state == Progressive.e_Error) {
                Toast.makeText(mContext, String.format("Failed to render the page No.%d failed!", index), Toast.LENGTH_LONG).show();
                return;
            }

            //Save the render result to the jpeg image.
            if (!Common.saveImageFile(bitmap, Bitmap.CompressFormat.JPEG, outputFilePath)) {
                Toast.makeText(mContext, String.format("Failed to Save Image File!"), Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(mContext, Common.runSuccesssInfo + outputFilePath, Toast.LENGTH_LONG).show();
        } catch (PDFException e) {
            Toast.makeText(mContext, String.format("Failed to render the page No.%d! %s", index, e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }
}
