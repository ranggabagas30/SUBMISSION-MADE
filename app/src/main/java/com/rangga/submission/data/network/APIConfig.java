package com.rangga.submission.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConfig {

    private APIConfig() { }

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/discover/";
    public static final String API_KEY = "ce0c1f2d4dad3e62d8c6861b153861fa";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static CustomInterceptor customInterceptor = new CustomInterceptor();
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
    private static Gson gson = new GsonBuilder().setLenient().create();
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = retrofitBuilder.build();

    public static <S> S createService(Class<S> serviceClass) {
        if (!okHttpClientBuilder.interceptors().contains(httpLoggingInterceptor) &&
            !okHttpClientBuilder.interceptors().contains(customInterceptor)) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            okHttpClientBuilder.addInterceptor(customInterceptor);
            retrofitBuilder.client(okHttpClientBuilder.build());
            retrofit = retrofitBuilder.build();
        }
        return retrofit.create(serviceClass);
    }

    private static class CustomInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            HttpUrl.Builder modifiedHttpUrlBuilder = chain.request().url().newBuilder();
            HttpUrl modifiedHttpUrl = modifiedHttpUrlBuilder.addQueryParameter("api_key", API_KEY).build();

            Request.Builder modifiedRequestBuilder = chain.request().newBuilder().url(modifiedHttpUrl);
            Request modifiedRequest = modifiedRequestBuilder.build();

            return chain.proceed(modifiedRequest);
        }
    }
}
