package com.rangga.submission.data.network.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListResponse extends PagingResponse {

    @SerializedName("results")
    private List<MovieResponse> results;

    public void setResults(List<MovieResponse> results){
        this.results = results;
    }

    public List<MovieResponse> getResults(){
        return results;
    }

}
