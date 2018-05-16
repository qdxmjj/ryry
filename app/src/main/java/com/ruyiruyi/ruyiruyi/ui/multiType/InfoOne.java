package com.ruyiruyi.ruyiruyi.ui.multiType;

public class InfoOne {
    public String title;
    public String content;
    public boolean hasLine;
    public boolean hasGoView;

    public InfoOne(String title, String content, boolean hasLine) {
        this.title = title;
        this.content = content;
        this.hasLine = hasLine;
        hasGoView = false;
    }

    public InfoOne(String title, String content, boolean hasLine, boolean hasGoView) {
        this.title = title;
        this.content = content;
        this.hasLine = hasLine;
        this.hasGoView = hasGoView;
    }

    public boolean isHasGoView() {
        return hasGoView;
    }

    public void setHasGoView(boolean hasGoView) {
        this.hasGoView = hasGoView;
    }

    public boolean isHasLine() {
        return hasLine;
    }

    public void setHasLine(boolean hasLine) {
        this.hasLine = hasLine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}