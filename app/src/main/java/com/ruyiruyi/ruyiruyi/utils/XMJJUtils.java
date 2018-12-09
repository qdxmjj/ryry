package com.ruyiruyi.ruyiruyi.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class XMJJUtils {
    private static final String TAG = XMJJUtils.class.getSimpleName();

    public static String decodeJsonByToken(String json, String token) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        byte[] decode = TripleDESUtil.decode(json.getBytes("UTF8"));
        String password = token.substring(24,48);
        byte[] bytes = TripleDESUtil.decryptMode(password, decode);
        String jsonResult = new String(TripleDESUtil.decode(bytes),"UTF8");
        //jsonResult = jsonResult.substring(0,jsonResult.lastIndexOf("}")+1);
        return jsonResult;
    }

    /*public static String encodeJsonByToken(String json,String token) throws UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String password = token.substring(24,48);
        byte[] bytes = new byte[0];
        try {
            bytes = TripleDESUtil.decode3DES(password, TripleDESUtil.encode(json.getBytes("UTF8")));
        } catch (Exception e) {

        }
        byte[] encode = TripleDESUtil.encode(bytes);
        String jsonResult = new String(encode,"UTF8");
        return jsonResult;
    }*/

    public static String encodeJsonByToken(String json,String token) throws UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String password = token.substring(24,48);
      /*  Log.e(TAG, "encodeJsonByToken: --" + json);
        Log.e(TAG, "encodeJsonByToken: ---" + password);

        Log.e(TAG, "encodeJsonByToken: --*---" +  Base64.encodeToString(json.getBytes(), Base64.DEFAULT));
        Log.e(TAG, "encodeJsonByToken: --*---" +  Base64.encodeToString(json.getBytes(), Base64.DEFAULT).getBytes("UTF8"));*/
        byte[] bytes = TripleDESUtil.encryptMode(password, Base64.encodeToString(json.getBytes(), Base64.DEFAULT).getBytes("UTF8"));
        Log.e(TAG, "encodeJsonByToken:----------*------- " + new String(bytes));
        byte[] encode = Base64.encodeToString(bytes, Base64.DEFAULT).getBytes("UTF8");
        String jsonResult = new String(encode);
        return jsonResult;
    }

    public static void main(String[] args) throws BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        String token = "E10ADC3949BA59ABBE56E057F20F883E";
        String password = token.substring(0,24);
        System.out.println(password);
        String json = "{\"data\":{\"age\":0,\"birthday\":null,\"createTime\":1522286740000,\"email\":\"\",\"firstAddCar\":0,\"gender\":1,\"headimgurl\":\"http://180.76.243.205:8383/images/userHeadimgurl/default/383614945.jpg\",\"id\":57,\"ml\":0.00,\"nick\":\"用户13589342270\",\"password\":\"caae68a7hif7b6no1f8stu3a2ye8cb2a\",\"payPwd\":\"\",\"phone\":\"13589342270\",\"qqInfoId\":0,\"remark\":\"\",\n" +
                "\"status\":2,\"token\":\"a9c1fa67f58a4b2186e569c5c604e6c8\",\"updateTime\":null,\"wxInfoId\":0},\n" +
                "\"msg\":\"登录成功\",\n" +
                "\"status\":111111}";
        String s = XMJJUtils.encodeJsonByToken(json, token);
        System.out.println(s);
        String s1 = XMJJUtils.decodeJsonByToken(s, token);
        System.out.println(s1);
    }
}
