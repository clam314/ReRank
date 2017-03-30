package com.clam314.rerank.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clam314 on 2017/3/30
 */

public class CategoryResult {

    @SerializedName("Android")
    @Expose
    private List<Result> Android;

    @SerializedName("iOS")
    @Expose
    private List<Result> iOS;

    @SerializedName("福利")
    @Expose
    private List<Result>  welfare;

    @SerializedName("前端")
    @Expose
    private List<Result>  frontEnd;

    @SerializedName("App")
    @Expose
    private List<Result> app;

    @SerializedName("拓展资源")
    @Expose
    private List<Result> resource;

    @SerializedName("瞎推荐")
    @Expose
    private List<Result> recommend;

    @SerializedName("休息视频")
    @Expose
    private List<Result> video;


    public List<Result> getAndroid() {
        return Android;
    }

    public void setAndroid(List<Result> android) {
        Android = android;
    }

    public List<Result> getiOS() {
        return iOS;
    }

    public void setiOS(List<Result> iOS) {
        this.iOS = iOS;
    }

    public List<Result> getWelfare() {
        return welfare;
    }

    public void setWelfare(List<Result> welfare) {
        this.welfare = welfare;
    }

    public List<Result> getFrontEnd() {
        return frontEnd;
    }

    public void setFrontEnd(List<Result> frontEnd) {
        this.frontEnd = frontEnd;
    }

    public List<Result> getApp() {
        return app;
    }

    public void setApp(List<Result> app) {
        this.app = app;
    }

    public List<Result> getResource() {
        return resource;
    }

    public void setResource(List<Result> resource) {
        this.resource = resource;
    }

    public List<Result> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<Result> recommend) {
        this.recommend = recommend;
    }

    public List<Result> getVideo() {
        return video;
    }

    public void setVideo(List<Result> video) {
        this.video = video;
    }
}
