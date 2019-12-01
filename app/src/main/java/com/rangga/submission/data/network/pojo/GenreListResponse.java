package com.rangga.submission.data.network.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreListResponse extends ErrorResponse{

	@SerializedName("genres")
	private List<GenreResponse> genres;

	public void setGenres(List<GenreResponse> genres){
		this.genres = genres;
	}

	public List<GenreResponse> getGenres(){
		return genres;
	}

	@Override
 	public String toString(){
		return 
			"GenreListResponse{" +
			"genres = '" + genres + '\'' + 
			"}";
		}
}