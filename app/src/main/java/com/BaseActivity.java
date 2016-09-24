package com;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.network.RequestGenerator;
import com.util.LogUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BaseActivity extends AppCompatActivity implements Callback, HttpImpl {
    private static final int CACHED_RESPONSE = 504;
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB
    private static final String TAG = LogUtils.makeLogTag(BaseActivity.class);
    private final OkHttpClient client = new OkHttpClient();
    private HttpImpl httpImpl;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Cache cache;
    private boolean NO_CACHE = false;
    protected boolean ONLY_CACHE = true;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                httpImpl.onHttpFailure(e);
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        final String resString = response.body().string();

        if (response.code() != CACHED_RESPONSE) {
            final int requestType = (int) response.request().tag();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    httpImpl.onHttpResponse(requestType, resString);
                }
            });
        } else {
            String url = response.request().url() != null ? response.request().url().toString() : null;
            getData((Integer) response.request().tag(), httpImpl, NO_CACHE, url);
            LogUtils.info(TAG, "Nothing found from http cache. Re-hitting http request without cache.");
        }
    }

    /**
     * @param requestType Type of the request.
     * @param obj
     * @param isCached    boolean value tell if resposne of the http request needs to be cached.
     */
    public void getData(int requestType, Object obj, boolean isCached, String url) {
        this.httpImpl = (HttpImpl) obj;
        RequestGenerator requestGenerator = new RequestGenerator();
        Request request = requestGenerator.buildRequest(requestType, isCached, url);

        if (cache == null) {
            cache = getHttpClientCache();
        }

        client.newBuilder().cache(cache).build();

        if (null != request) {
            client.newCall(request).enqueue(this);
        }
    }

    @Override
    public void onHttpFailure(IOException e) {

    }

    @Override
    public void onHttpResponse(int requestType, String response) {

    }

    private static Cache getHttpClientCache() {
        File cacheDir = AppUtils.getInstance().getContext().getDir("service_api_cache", Context.MODE_PRIVATE);
        return new Cache(cacheDir, HTTP_CACHE_SIZE);
    }
}