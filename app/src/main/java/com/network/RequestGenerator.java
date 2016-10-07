package com.network;


import com.util.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Request;

public class RequestGenerator {

    private final static String DOMAIN_CONNECT_URL_WITH_PROTOCOL = "https://admob-app-id-9589461173.firebaseapp.com/wallpaper/json";
    private final static String WALLPAPER_CATEGORY_URL = DOMAIN_CONNECT_URL_WITH_PROTOCOL + "/wallpaper_category.json";
    private final static String NETWORK_INFO_URL = "http://www.ip-api.com/json";
    private static final int MAX_CACHE_DAYS = 1;
    private Request.Builder mRequestBuilder;

    /**
     * This method build HTTP request.
     * Also set URL based on the request type.
     *
     * @param requestType type of the request. Defined in App constant.
     * @param isCached    if http caching required.
     * @param urlToHit
     * @return build a http Request.
     */
    public Request buildRequest(int requestType, boolean isCached, String urlToHit) {
        Request request;
        if (null != urlToHit) {
            request = getRequestBuilder(isCached).tag(requestType).url(urlToHit).build();
        } else if (requestType == AppConstants.WALLPAPER_CATEGORY_REQUEST) {
            request = getRequestBuilder(isCached).tag(requestType).url(WALLPAPER_CATEGORY_URL).build();
        } else {
            request = getRequestBuilder(isCached).tag(requestType).url(NETWORK_INFO_URL).build();
        }
        return request;
    }

    private Request.Builder getRequestBuilder(boolean isCached) {
        //Check if request builder is null.
        if (mRequestBuilder == null) {
            mRequestBuilder = new Request.Builder();
        }

        //Set cache type and duration in request builder.
        if (isCached) {
            mRequestBuilder.cacheControl(new CacheControl.Builder().onlyIfCached().maxStale(MAX_CACHE_DAYS, TimeUnit.DAYS).build());
        } else {
            mRequestBuilder.cacheControl(new CacheControl.Builder().noCache().maxStale(MAX_CACHE_DAYS, TimeUnit.DAYS).build());
        }
        return mRequestBuilder;
    }
}