package com.ruyiruyi.ruyiruyi.ui.model;

public class StrObj {
    public String str;

    public StrObj(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"str\":\"")
                .append(str).append('\"');
        sb.append('}');
        return sb.toString();
    }
}