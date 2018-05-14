package com.ruyiruyi.merchant.utils;

public class MyNote {


    /*
    门店注册页面门店类别、城市列表、合作项目  下载与存储
    private void initRegisterServiceTypeData() {
        date_serviceType = new Date();
        List<ServiceType> serviceTypeList = null;
        try {
            serviceTypeList = new DbConfig().getDbManager().selector(ServiceType.class).orderBy("time").findAll();

        } catch (DbException e) {
        }
        JSONObject object = new JSONObject();

        try {
            if (serviceTypeList == null) {
                object.put("time", "2000-00-00 00:00:00");
            } else {
                String time = serviceTypeList.get(serviceTypeList.size() - 1).getTime();
                object.put("time", time);

            }
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreServiceType");
        params.addBodyParameter("reqJson", object.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            JSONArray data = jsonObject.getJSONArray("data");
                            saveServiceTypeToDb(data);
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                }

        );
    }

    private void initRegisterCategoryData() {
        date_category = new Date();
        List<Category> categoryList = null;
        try {
            categoryList = new DbConfig().getDbManager().selector(Category.class).orderBy("time").findAll();

        } catch (DbException e) {
        }
        JSONObject object = new JSONObject();

        try {
            if (categoryList == null) {
                object.put("time", "2000-00-00 00:00:00");
            } else {
                String time = categoryList.get(categoryList.size() - 1).getTime();
                object.put("time", time);

            }
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreType");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "initRegisterCategoryData: ------------1");
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "initRegisterCategoryData: ------------2 result = " + result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            JSONArray data = jsonObject.getJSONArray("data");
                            saveCategoryToDb(data);
                            Log.e(TAG, "initRegisterCategoryData: ------------3 data = " + data);
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                }

        );
    }

    private void initProvinceData() {
        date = new Date();
        List<Province> provinceList = null;
        try {
            provinceList = new DbConfig().getDbManager().selector(Province.class).orderBy("time").findAll();
        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();
        try {
            //时间请求 ！！
            if (provinceList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time", time);
                Log.e(TAG, "initProvinceData: time = " + time);
            }

        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initProvinceData: params = " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.e(TAG, "onSuccess: getData and To Db ??  status = " + status + "msg = " + msg + "data = " + data.toString());
                    saveProvinceToDb(data);
                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: getData and To Db ?? ");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: getData and To Db ??");
            }

            @Override
            public void onFinished() {

            }
        });
    }*/


    /* OpenOrder 去掉替换功能  的TextView
    *                     <TextView
                        android:id="@+id/code_b_change"
                        android:layout_width="wrap_content"
                        android:layout_height="match_parent"
                        android:layout_gravity="right"
                        android:layout_marginBottom="10dp"
                        android:layout_marginRight="10dp"
                        android:layout_marginTop="10dp"
                        android:background="@drawable/register_w_button"
                        android:gravity="center_vertical"
                        android:paddingLeft="30dp"
                        android:paddingRight="30dp"
                        android:text="替换"
                        android:textColor="@color/c6"
                        android:textSize="18sp" />
    * */
}