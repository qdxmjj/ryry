package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.net.Uri;

public class EvaluateImage {
    public Uri uri;
    public Boolean isAdd;

    public EvaluateImage() {
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Boolean getAdd() {
        return this.isAdd;
    }

    public void setAdd(Boolean add) {
        this.isAdd = add;
    }
}