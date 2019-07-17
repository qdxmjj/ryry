package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.foxit.sdk.PDFViewCtrl;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.common.Library;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.Common;
import com.ruyiruyi.merchant.cell.Signature;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class PdfSignActivity extends AppCompatActivity {

    private TextView tv_sign;

    private PDFViewCtrl pdfViewCtrl = null;

    private static final String sn = "M57TflRHQxawVVZTLMbkDGqK9BylUuIq+HrHMG3D/p87484wVXdh9w==";
    private static final String key = "ezJvjl/GtGp399sXJVWqfPXQzv1oSAOWRfbAGDU5kS7VglVhz3CKxKPhSadn2uWcfJi0Gn8BD1Qs82vlUUgUXcRDQ0Uw6341X+4m1lhkzS+EUlzZfhisKeeVJrFYntgEVH4sDPAy3kOWT+UJY20NXisfXih+Z3jSSmdspG/ExxBq9BQ6ZrQt0fdgOJZctF+ofMYs/nBJYs1k/TrY9FIlLBsjLmFlGkOqI/fLuKwO3xeCcVjKkSHXy5LShO285jQH19Rd7kPQX5fNYK+qn5p/K/TBSQqq7hVpTmCPj5oczcJ9Ds1LM9O3GITbo1yLZwwsRfIjD23UHUsJOKS/vhUXRKOCqiR29eJAV8n8jjQVZukOIoiZ+njIKmEG8nuAV8nsJUeHP380rKCMQGG0j7QVx+IlzxQQ44utppGknMqCTkXvQ/IoLHxBgwl3DXab/ThMPWinH8xvLd8Lop0imO7mcmaU4xXE4gCUT8p+dlVFwfF9d7uzZjRpCuigkchEyLTxBERMAWt339RNC8RDC/QALGbwOD6rzYZqYRF6bZVgp9X8CqzgyKtfhzKrcqsjkqZEXSORDF769ReyO//JafLY5UqzkRry+mC8tEvyY5GPTf1DUrgu8IQR6hAJWnAKP1SV62qci8t1w3Gx/3CBRWqf2hMrb8amxGvvAD5W1bJIeAnFCswxLs09xFB14ueEOMQ9jwWEMvk+1rhSwQlP0N4/JDWAjxicWfw+CFbSfgHQUcr9FcQXTc1OB5wd1Cc2/Px9bpeJrHEmh9xw2kE207tQn7QK/PWTi+7Duwlro4ZhIRwuy4nnv8HG4h7BuJoBIqz+WW5OIUPTYXu375DJWMxq/IG1JKY7glmmBSgu2Kjc1UH0jlGZ5btorm58EnCxIyoCfWGb7xD/w3Hvm6+r+w8mN/IqXEeXI/hk6NAgD/0usTmX1NKkTD8k4/bwr+qTXlbPCJpb3OT6WZpczpipF9OiOCmZFlMN4kRjqjIel9IRowUHEzXbxOskdh50E6LarZiDX7PxofohDS4vWdgkcr6G4i8UzAzKRZzNWALf8BmZHwxUH9LMr25d1Igq63510Od0kSnMpGIlod+xyR04NhjmA0TTjz0hsiyBHOBWe89vPUJjD9PQprmTUKmNDeKI2cZ46wacXkawvFHuau2t37lmkj5/76QAaSlnmVXyWArB0GAV9OYKphW9XYbkyGVCcAk1pkE3zmfVSekUYAnwbsJvGjv5RRNZOZYAO1BeP7J+sBPxnD+sdSkPc+DhDQ==";

    private int initErrCode = Constants.e_ErrSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_pdf);

        tv_sign = findViewById(R.id.tv_sign);

        if (!Common.checkSD()) {
            Toast.makeText(PdfSignActivity.this, "Error: Directory of SD is not exist!", Toast.LENGTH_LONG).show();
            return;
        }

        initErrCode = Library.initialize(sn, key);
        showLibraryErrorInfo();


        RxViewAction.clickNoDouble(tv_sign).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //打开PDF准备签名
                if (initErrCode != Constants.e_ErrSuccess) {
                    showLibraryErrorInfo();
                    return;
                }

                String testFilePath = Common.getFixFolder() + Common.signatureInputFile;
                String certPath = Common.getFixFolder() + Common.signatureCertification;
                String certPassword = "123456";
                Signature signature = new Signature(getApplicationContext(), testFilePath, certPath, certPassword);
                signature.addSignature(0);
            }
        });

    }


    private void showLibraryErrorInfo() {
        if (initErrCode != Constants.e_ErrSuccess) {
            if (initErrCode == Constants.e_ErrInvalidLicense) {
                Toast.makeText(getApplicationContext(), "The license is invalid!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to initialize the library!", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }
}
