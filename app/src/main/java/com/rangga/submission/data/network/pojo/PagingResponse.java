package com.rangga.submission.data.network.pojo;

import com.google.gson.annotations.SerializedName;

public class PagingResponse extends ErrorResponse{

	@SerializedName("page")
	public int page;

	@SerializedName("total_pages")
	public int totalPages;

	@SerializedName("total_results")
	public int totalResults;

	@Override
 	public String toString(){
		return 
			"PagingResponse{" +
			"page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' +
			",total_results = '" + totalResults + '\'' + 
			"}";
		}
}