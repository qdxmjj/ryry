package com.ruyiruyi.ruyiruyi.ui.model;

import com.ruyiruyi.ruyiruyi.ui.activity.Line;
import com.ruyiruyi.ruyiruyi.ui.multiType.Cxwy;

import java.util.List;

/**
 * Created by Lenovo on 2018/9/18.
 */

public class CxwyYear {
    public int serviceYear;
    public List<CxwyPrice> cxwyPriceList;

    public CxwyYear(int serviceYear, List<CxwyPrice> cxwyPriceList) {
        this.serviceYear = serviceYear;
        this.cxwyPriceList = cxwyPriceList;
    }

    public int getServiceYear() {
        return serviceYear;
    }

    public void setServiceYear(int serviceYear) {
        this.serviceYear = serviceYear;
    }

    public List<CxwyPrice> getCxwyPriceList() {
        return cxwyPriceList;
    }

    public void setCxwyPriceList(List<CxwyPrice> cxwyPriceList) {
        this.cxwyPriceList = cxwyPriceList;
    }
}
