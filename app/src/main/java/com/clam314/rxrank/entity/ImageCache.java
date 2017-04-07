package com.clam314.rxrank.entity;

/**
 * Created by clam314 on 2017/4/7
 */

public class ImageCache {

    private long refreshTime;

    private String savePath;

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
