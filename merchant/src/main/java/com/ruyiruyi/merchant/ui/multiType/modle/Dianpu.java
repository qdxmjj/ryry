package com.ruyiruyi.merchant.ui.multiType.modle;


import android.os.Parcel;
import android.os.Parcelable;

public class Dianpu implements Parcelable {


    private String orderImage;
    private String orderServcieTypeName;
    private String orderPrice;
    private String orderActuallyPrice;
    private String orderState;
    private long orderTime;

    private String orderServcieTypeName_First;

    private String orderNo;
    private String orderType;

    public void setOrderActuallyPrice(String orderActuallyPrice) {
        this.orderActuallyPrice = orderActuallyPrice;
    }

    public String getOrderActuallyPrice() {
        return orderActuallyPrice;
    }

    protected Dianpu(Parcel in) {
        orderImage = in.readString();
        orderServcieTypeName = in.readString();
        orderPrice = in.readString();
        orderState = in.readString();
        orderTime = in.readLong();
        orderServcieTypeName_First = in.readString();
        orderNo = in.readString();
        orderType = in.readString();
    }

    public static final Creator<Dianpu> CREATOR = new Creator<Dianpu>() {
        @Override
        public Dianpu createFromParcel(Parcel in) {
            return new Dianpu(in);
        }

        @Override
        public Dianpu[] newArray(int size) {
            return new Dianpu[size];
        }
    };


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderImage);
        parcel.writeString(orderServcieTypeName);
        parcel.writeString(orderPrice);
        parcel.writeString(orderState);
        parcel.writeLong(orderTime);
        parcel.writeString(orderServcieTypeName_First);
        parcel.writeString(orderNo);
        parcel.writeString(orderType);
    }




    public void setOrderServcieTypeName_First(String orderServcieTypeName_First) {
        this.orderServcieTypeName_First = orderServcieTypeName_First;
    }

    public String getOrderServcieTypeName_First() {
        return orderServcieTypeName_First;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public void setOrderServcieTypeName(String orderServcieTypeName) {
        this.orderServcieTypeName = orderServcieTypeName;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public String getOrderServcieTypeName() {
        return orderServcieTypeName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getOrderState() {
        return orderState;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public Dianpu() {
    }


}