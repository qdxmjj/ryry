package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/10/12 9:32
 */

public class PutForwardInfo {
    private double putForwardMoney;
    private int putForwardType;//1 支付宝 2 微信
    private int putForwardStatus;//1 提现中 2 成功 3 失败
    private long putForwardTime;
    private String remark;//备注
    private String orderNo;//订单编号
    private int bigType;//收支类型 1支出 2 收入

    public int getBigType() {
        return bigType;
    }

    public void setBigType(int bigType) {
        this.bigType = bigType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getPutForwardMoney() {
        return putForwardMoney;
    }

    public void setPutForwardMoney(double putForwardMoney) {
        this.putForwardMoney = putForwardMoney;
    }

    public int getPutForwardType() {
        return putForwardType;
    }

    public void setPutForwardType(int putForwardType) {
        this.putForwardType = putForwardType;
    }

    public int getPutForwardStatus() {
        return putForwardStatus;
    }

    public void setPutForwardStatus(int putForwardStatus) {
        this.putForwardStatus = putForwardStatus;
    }

    public long getPutForwardTime() {
        return putForwardTime;
    }

    public void setPutForwardTime(long putForwardTime) {
        this.putForwardTime = putForwardTime;
    }
}
