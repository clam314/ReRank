package com.clam314.rerank.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by clam314 on 2017/3/30
 */

public class Result {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("results")
    @Expose
    private CategoryResult results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public CategoryResult getResults() {
        return results;
    }

    public void setResults(CategoryResult results) {
        this.results = results;
    }
}
