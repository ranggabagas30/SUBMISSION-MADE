package com.rangga.submission.data.network.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvListResponse extends PagingResponse {

    @SerializedName("results")
    private List<TvResponse> results;

    public void setResults(List<TvResponse> results){
        this.results = results;
    }

    public List<TvResponse> getResults(){
        return results;
    }
}
