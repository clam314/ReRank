package com.clam314.rerank.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryDayResult {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("results")
    @Expose
    private List<String> results = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

}