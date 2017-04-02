package com.clam314.rxrank.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clam314 on 2017/3/30
 */

public class CategoryGroup implements Parcelable {

    private String day;

    @SerializedName("Android")
    @Expose
    private ArrayList<Item> Android;

    @SerializedName("iOS")
    @Expose
    private ArrayList<Item> iOS;

    @SerializedName("福利")
    @Expose
    private ArrayList<Item>  welfare;

    @SerializedName("前端")
    @Expose
    private ArrayList<Item>  frontEnd;

    @SerializedName("App")
    @Expose
    private ArrayList<Item> app;

    @SerializedName("拓展资源")
    @Expose
    private ArrayList<Item> resource;

    @SerializedName("瞎推荐")
    @Expose
    private ArrayList<Item> recommend;

    @SerializedName("休息视频")
    @Expose
    private ArrayList<Item> video;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<Item> getAndroid() {
        return Android;
    }

    public void setAndroid(ArrayList<Item> android) {
        Android = android;
    }

    public ArrayList<Item> getiOS() {
        return iOS;
    }

    public void setiOS(ArrayList<Item> iOS) {
        this.iOS = iOS;
    }

    public ArrayList<Item> getWelfare() {
        return welfare;
    }

    public void setWelfare(ArrayList<Item> welfare) {
        this.welfare = welfare;
    }

    public ArrayList<Item> getFrontEnd() {
        return frontEnd;
    }

    public void setFrontEnd(ArrayList<Item> frontEnd) {
        this.frontEnd = frontEnd;
    }

    public ArrayList<Item> getApp() {
        return app;
    }

    public void setApp(ArrayList<Item> app) {
        this.app = app;
    }

    public ArrayList<Item> getResource() {
        return resource;
    }

    public void setResource(ArrayList<Item> resource) {
        this.resource = resource;
    }

    public ArrayList<Item> getRecommend() {
        return recommend;
    }

    public void setRecommend(ArrayList<Item> recommend) {
        this.recommend = recommend;
    }

    public ArrayList<Item> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<Item> video) {
        this.video = video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day);
        dest.writeTypedList(this.Android);
        dest.writeTypedList(this.iOS);
        dest.writeTypedList(this.welfare);
        dest.writeTypedList(this.frontEnd);
        dest.writeTypedList(this.app);
        dest.writeTypedList(this.resource);
        dest.writeTypedList(this.recommend);
        dest.writeTypedList(this.video);
    }

    public CategoryGroup() {
    }

    protected CategoryGroup(Parcel in) {
        this.day = in.readString();
        this.Android = in.createTypedArrayList(Item.CREATOR);
        this.iOS = in.createTypedArrayList(Item.CREATOR);
        this.welfare = in.createTypedArrayList(Item.CREATOR);
        this.frontEnd = in.createTypedArrayList(Item.CREATOR);
        this.app = in.createTypedArrayList(Item.CREATOR);
        this.resource = in.createTypedArrayList(Item.CREATOR);
        this.recommend = in.createTypedArrayList(Item.CREATOR);
        this.video = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Parcelable.Creator<CategoryGroup> CREATOR = new Parcelable.Creator<CategoryGroup>() {
        @Override
        public CategoryGroup createFromParcel(Parcel source) {
            return new CategoryGroup(source);
        }

        @Override
        public CategoryGroup[] newArray(int size) {
            return new CategoryGroup[size];
        }
    };
}
