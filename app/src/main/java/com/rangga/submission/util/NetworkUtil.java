package com.rangga.submission.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.rangga.submission.BuildConfig;
import com.rangga.submission.R;

import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtil {
    public static String handleApiError(Context context, Throwable error) {
        final int API_STATUS_CODE_LOCAL_ERROR = 0;
        String errorMessage;
        if (error instanceof HttpException) {
            switch (((HttpException) error).code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    errorMessage = "Unauthorized User";
                    break;
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    errorMessage = "Forbidden (Code: 404)";
                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    errorMessage = "Server Problem (Code: 500)";
                    break;
                case HttpsURLConnection.HTTP_BAD_REQUEST:
                    errorMessage = "Bad Request (Code: 400)";
                    break;
                case API_STATUS_CODE_LOCAL_ERROR:
                    errorMessage = "API status code local error (Code: 0)";
                    break;
                default:
                    errorMessage = error.getLocalizedMessage();
            }
        } else if (error instanceof JsonSyntaxException) {
            errorMessage = "Something Went Wrong API is not responding properly! (Code: 666)";
        } else if (error instanceof UnknownHostException) {
            errorMessage = "Slow or no internet connection";
        } else{
            errorMessage = error.getMessage();
        }

        Log.e("Network error", "ERROR: " + errorMessage, error);
        if (!BuildConfig.DEBUG) errorMessage = context.getString(R.string.error_get_data);
        return errorMessage;
    }
}
