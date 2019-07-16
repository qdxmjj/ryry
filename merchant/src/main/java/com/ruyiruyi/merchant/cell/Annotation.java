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

import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.fxcrt.RectFArray;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextSearch;
import com.foxit.sdk.pdf.annots.Annot;
import com.foxit.sdk.pdf.annots.Note;
import com.foxit.sdk.pdf.annots.QuadPoints;
import com.foxit.sdk.pdf.annots.QuadPointsArray;
import com.foxit.sdk.pdf.annots.TextMarkup;

public class Annotation {
    private String mFilePath = "";
    private Context mContext = null;

    private String searchText[] = {"Highlight_1", "Highlight_2", "Highlight_3",
            "Underline_1", "Underline_2", "Underline_3",
            "Strikeout_1", "Strikeout_2", "Strikeout_3",
            "Squiggly_1", "Squiggly_2", "Squiggly_3",};

    private int color[] = {0xFFFF0000, 0xFF00FF00, 0xFF0000FF};
    private float opacity[] = {0.2f, 0.6f, 0.9f};
    private int textMarkupType[] = {Annot.e_Highlight, Annot.e_Underline, Annot.e_StrikeOut, Annot.e_Squiggly};

    public Annotation(Context context, String pdfFilePath) {
        mFilePath = pdfFilePath;
        mContext = context;
    }

    public void addAnnotation() {
        int indexPdf = mFilePath.lastIndexOf(".");
        int indexSep = mFilePath.lastIndexOf("/");
        String filenameWithoutPdf = mFilePath.substring(indexSep + 1, indexPdf);
        String outputFilePath = Common.getOutputFilesFolder(Common.annotationModuleName) + filenameWithoutPdf + "_add.pdf";

        PDFDoc doc = Common.loadPDFDoc(mContext, mFilePath, null);
        if (doc == null) {
            return;
        }

        try {
            //Add Note annotations.
            for (int i = 0; i < 3; i++) {
                Annot annot =addAnnotation(doc, 0, Annot.e_Note, new RectF(100 + i * 160, 180, 120 + i * 160,200 ));
                Note noteAnnot = new Note(annot);
                if (null == noteAnnot || noteAnnot.isEmpty()) {
                    continue;
                }
                noteAnnot.setIconName("Comment");
                noteAnnot.setBorderColor(color[i % 3]);

                //It should be reset appearance after being modified.
                noteAnnot.resetAppearanceStream();
            }

            //Add the TextMarkup annotations.
            for (int i = 0; i < searchText.length; i++) {
                TextMarkup textMarkupAnnot = addTextMarkup(doc, searchText[i], textMarkupType[i / 3]);
                if (textMarkupAnnot == null || textMarkupAnnot.isEmpty()) {
                    continue;
                }
                textMarkupAnnot.setBorderColor(color[i % 3]);
                textMarkupAnnot.setOpacity(opacity[i % 3]);

                //It should be reset appearance after being modified.
                textMarkupAnnot.resetAppearanceStream();
            }

            doc.saveAs(outputFilePath, PDFDoc.e_SaveFlagNormal);
        } catch (PDFException e) {
            Toast.makeText(mContext, "Add annotation demo run error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(mContext, Common.runSuccesssInfo + outputFilePath, Toast.LENGTH_LONG).show();
        return;
    }

    //Add the TextMarkup annotations.
    private TextMarkup addTextMarkup(PDFDoc doc, String keywords, int annotType) {
        if (annotType != Annot.e_Squiggly
                && annotType != Annot.e_StrikeOut
                && annotType != Annot.e_Highlight
                && annotType != Annot.e_Underline) {
            return null;
        }

        TextSearch textSearch = null;
        TextMarkup textMarkupAnnot = null;
        try {
            //Firstly, search the text.
            textSearch = new TextSearch(doc, null);
            if (textSearch == null || textSearch.isEmpty()) {
                Toast.makeText(mContext, "create text search error", Toast.LENGTH_LONG).show();
                return null;
            }

            if (!textSearch.setPattern(keywords)) {
                Toast.makeText(mContext, "set keywords error", Toast.LENGTH_LONG).show();
                textSearch.delete();
                return null;
            }

            boolean bMatch = textSearch.findNext();
            if (bMatch) {
                RectFArray rectFArray = textSearch.getMatchRects();
                int rectCount = rectFArray.getSize();

                //Next, calculate the quadPoints according to the matching rectangle.
                QuadPointsArray quadPointsArray = new QuadPointsArray();

                for (int i = 0; i < rectCount; i++) {
                    RectF textRectF = rectFArray.getAt(i);

                    QuadPoints quadPoints = new QuadPoints();
                    quadPoints.setFirst(new PointF(textRectF.getLeft(), textRectF.getTop()));
                    quadPoints.setSecond(new PointF(textRectF.getRight(), textRectF.getTop()));
                    quadPoints.setThird(new PointF(textRectF.getLeft(), textRectF.getBottom()));
                    quadPoints.setFourth(new PointF(textRectF.getRight(), textRectF.getBottom()));

                    quadPointsArray.add(quadPoints);
                }

                int pageIndex = textSearch.getMatchPageIndex();

                //Finally, add the TextMarkup annotation to the matching page.
                textMarkupAnnot = new TextMarkup(addAnnotation(doc, pageIndex, annotType, new RectF(0, 0, 0, 0)));
                textMarkupAnnot.setQuadPoints(quadPointsArray);
            }
        } catch (PDFException e) {
            Toast.makeText(mContext, "Get text rect error. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return textMarkupAnnot;
    }

    private Annot addAnnotation(PDFDoc doc, int pageIndex, int annotType, RectF rect) {
        if (rect == null) {
            return null;
        }

        Annot annot = null;
        try {
            PDFPage page = doc.getPage(pageIndex);
            if (page == null || page.isEmpty()) {
                return null;
            }
            annot = page.addAnnot(annotType, rect);
            if (annot != null && !annot.isEmpty()) {
                //Set the unique ID to the annotation.
                String uuid = Common.randomUUID(null);
                annot.setUniqueID(uuid);

                //Set flags to the annotation.
                annot.setFlags(4);

                //Set the modified datetime to the annotation.
                DateTime dateTime = Common.getCurrentDateTime();
                annot.setModifiedDateTime(dateTime);
            }
        } catch (PDFException e) {
            Toast.makeText(mContext, "Add annot error. " + e.getMessage(), Toast.LENGTH_LONG);
        }
        return annot;
    }
}
