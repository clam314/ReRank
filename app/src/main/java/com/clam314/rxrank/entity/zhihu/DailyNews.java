package com.clam314.rxrank.entity.zhihu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyNews implements Parcelable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("stories")
    @Expose
    private List<Story> stories = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        dest.writeList(this.stories);
    }

    public DailyNews() {
    }

    protected DailyNews(Parcel in) {
        this.date = in.readString();
        this.stories = new ArrayList<Story>();
        in.readList(this.stories, Story.class.getClassLoader());
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