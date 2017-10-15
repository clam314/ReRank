package com.clam314.rxrank.entity.zhihu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clam314 on 2017/10/15
 */

public class Themes {
    @SerializedName("limit")
    @Expose
    private Long limit;
    @SerializedName("others")
    @Expose
    private List<Theme> others = null;

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public List<Theme> getOthers() {
        return others;
    }

    public void setOthers(List<Theme> others) {
        this.others = others;
    }

}
