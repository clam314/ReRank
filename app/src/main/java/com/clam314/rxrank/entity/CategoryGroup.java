package com.clam314.rxrank.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clam314 on 2017/3/30
 */

public class CategoryGroup {

    @SerializedName("Android")
    @Expose
    private List<Item> Android;

    @SerializedName("iOS")
    @Expose
    private List<Item> iOS;

    @SerializedName("福利")
    @Expose
    private List<Item>  welfare;

    @SerializedName("前端")
    @Expose
    private List<Item>  frontEnd;

    @SerializedName("App")
    @Expose
    private List<Item> app;

    @SerializedName("拓展资源")
    @Expose
    private List<Item> resource;

    @SerializedName("瞎推荐")
    @Expose
    private List<Item> recommend;

    @SerializedName("休息视频")
    @Expose
    private List<Item> video;


    public List<Item> getAndroid() {
        return Android;
    }

    public void setAndroid(List<Item> android) {
        Android = android;
    }

    public List<Item> getiOS() {
        return iOS;
    }

    public void setiOS(List<Item> iOS) {
        this.iOS = iOS;
    }

    public List<Item> getWelfare() {
        return welfare;
    }

    public void setWelfare(List<Item> welfare) {
        this.welfare = welfare;
    }

    public List<Item> getFrontEnd() {
        return frontEnd;
    }

    public void setFrontEnd(List<Item> frontEnd) {
        this.frontEnd = frontEnd;
    }

    public List<Item> getApp() {
        return app;
    }

    public void setApp(List<Item> app) {
        this.app = app;
    }

    public List<Item> getResource() {
        return resource;
    }

    public void setResource(List<Item> resource) {
        this.resource = resource;
    }

    public List<Item> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<Item> recommend) {
        this.recommend = recommend;
    }

    public List<Item> getVideo() {
        return video;
    }

    public void setVideo(List<Item> video) {
        this.video = video;
    }
}
