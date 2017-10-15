package com.clam314.rxrank.entity.zhihu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clam314 on 2017/10/15
 */

public class Sections {
    @SerializedName("data")
    @Expose
    private List<Section> data = null;

    public List<Section> getData() {
        return data;
    }

    public void setData(List<Section> data) {
        this.data = data;
    }
}
