package com.ruyiruyi.ruyiruyi.ui.multiType;

public class GoodsClass {
    public int classId;
    public String className;
    public String classImage;

    public GoodsClass(int classId, String className, String classImage) {
        this.classId = classId;
        this.className = className;
        this.classImage = classImage;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassImage() {
        return classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }
}