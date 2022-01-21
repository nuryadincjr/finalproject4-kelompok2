package com.nuryadincjr.ebusantara.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Constant {
    public static final String TITLE_VIW_ONLY = "View only";
    public static final int RC_SIGN_IN = 0;

    public static Bitmap getQrCode(String code){
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 700, 700);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
