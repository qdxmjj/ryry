package com.ruyiruyi.merchant.utils;

public class UtilsURL {
    public static final String LOGIN_PASS_REQUEST_URL_BIRD_SERVER = "http://192.168.0.96:8060/";//鸟
    public static final String LOGIN_PASS_REQUEST_URL_LOCAL_SERVER = "http://192.168.0.167:8082/xmjj-webservice/";//本地服务器赛扬
    public static final String LOGIN_PASS_REQUEST_URL_WEB_SERVER_OLD = "http://ruyiruyi.s1.natapp.cc/xmjj-webservice/";//外网1
    public static final String LOGIN_PASS_REQUEST_URL_WEB_SERVER_RELEASE = "http://180.76.243.205:10002/xmjj-webservice/";//外网正式
    public static String REQUEST_URL_GONGLIN_TEST = "http://192.168.0.86:8060/";//龚林
    public static String REQUEST_URL_GONGLIN_FAHUO_TEST = "http://192.168.0.86:8030/";//龚林 测试 发货
    public static String REQUEST_URL_FAHUO_RELEASE = "http://180.76.243.205:10004/";// 发货正式
    public static String REQUEST_URL_FAHUO = REQUEST_URL_FAHUO_RELEASE;
    public static final String REQUEST_URL = LOGIN_PASS_REQUEST_URL_WEB_SERVER_RELEASE;

}