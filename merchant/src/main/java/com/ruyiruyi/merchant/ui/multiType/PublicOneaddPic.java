package com.ruyiruyi.merchant.ui.multiType;

public class PublicOneaddPic {
    private String title;
    private String content;
    private boolean hasRightPic;
    private boolean hasBottomLine;

    public PublicOneaddPic() {
    }

    public PublicOneaddPic(String title, String content, boolean hasRightPic, boolean hasBottomLine) {
        this.title = title;
        this.content = content;
        this.hasRightPic = hasRightPic;
        this.hasBottomLine = hasBottomLine;
    }

    public void setHasBottom(boolean hasBottom) {
        this.hasBottomLine = hasBottom;
    }

    public boolean isHasBottom() {
        return hasBottomLine;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHasPic(boolean hasPic) {
        this.hasRightPic = hasPic;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isHasPic() {
        return hasRightPic;
    }
}