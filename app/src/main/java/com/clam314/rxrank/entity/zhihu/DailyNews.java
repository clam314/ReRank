package com.clam314.rxrank.entity.zhihu;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyNews implements Parcelable {

    @SerializedName("date")
    @Expose
    private String date;
    private String showDate;
    @SerializedName("stories")
    @Expose
    private List<Story> stories = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.showDate);
        dest.writeTypedList(this.stories);
    }

    public DailyNews() {
    }

    protected DailyNews(Parcel in) {
        this.date = in.readString();
        this.showDate = in.readString();
        this.stories = in.createTypedArrayList(Story.CREATOR);
    }

    public static final Creator<DailyNews> CREATOR = new Creator<DailyNews>() {
        @Override
        public DailyNews createFromParcel(Parcel source) {
            return new DailyNews(source);
        }

        @Override
        public DailyNews[] newArray(int size) {
            return new DailyNews[size];
        }
    };
}