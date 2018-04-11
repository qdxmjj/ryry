package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "tiretype")
public class TireType {

    @Column(name = "id",isId = true,autoGen = false)
    private int tireTypeId;

    @Column(name = "tireflatwidth")
    private String tireFlatWidth;

    @Column(name = "tireflatnessratio")
    private String tireFlatnessRatio;

    @Column(name = "tirediameter")
    private String tireDiameter;


    @Column(name = "time")
    private String time;

    public TireType() {
    }

    public TireType(int tireTypeId, String tireFlatWidth, String tireFlatnessRatio, String tireDiameter, String time) {
        this.tireTypeId = tireTypeId;
        this.tireFlatWidth = tireFlatWidth;
        this.tireFlatnessRatio = tireFlatnessRatio;
        this.tireDiameter = tireDiameter;
        this.time = time;
    }

    public int getTireTypeId() {
        return tireTypeId;
    }

    public void setTireTypeId(int tireTypeId) {
        this.tireTypeId = tireTypeId;
    }

    public String getTireFlatWidth() {
        return tireFlatWidth;
    }

    public void setTireFlatWidth(String tireFlatWidth) {
        this.tireFlatWidth = tireFlatWidth;
    }

    public String getTireFlatnessRatio() {
        return tireFlatnessRatio;
    }

    public void setTireFlatnessRatio(String tireFlatnessRatio) {
        this.tireFlatnessRatio = tireFlatnessRatio;
    }

    public String getTireDiameter() {
        return tireDiameter;
    }

    public void setTireDiameter(String tireDiameter) {
        this.tireDiameter = tireDiameter;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}