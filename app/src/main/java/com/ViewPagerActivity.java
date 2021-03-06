/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ViewPagerAdapter;
import com.model.ImageListDTO;
import com.startapp.android.publish.StartAppAd;
import com.util.AppConstants;
import com.util.GsonUtils;
import com.wallpaperhs.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

import static android.view.View.GONE;

public class ViewPagerActivity extends BaseActivity implements View.OnClickListener {
    String urlToHit = "";
    List<String> url_list = new ArrayList<>();
    private ViewPagerAdapter adapter = new ViewPagerAdapter(url_list);
    private RelativeLayout rl_loading;
    private AVLoadingIndicatorView avi;
    private TextView tv_set_wallpaper;
    private TextView tv_share;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        urlToHit = getIntent().getExtras().getString(MainActivity.URL_FOR_IMAGE_LIST);

        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        tv_set_wallpaper = (TextView) findViewById(R.id.tv_set_wallpaper);
        tv_share = (TextView) findViewById(R.id.tv_share);

        showsplashAd(this, savedInstanceState);
        //Start Animation
        avi.smoothToShow();

        //setContentView(mViewPager);
        mViewPager.setAdapter(adapter);

        populateDataFromServer();

        //Set OnclickListner
        tv_share.setOnClickListener(this);
        tv_set_wallpaper.setOnClickListener(this);
    }

    private void showsplashAd(final ViewPagerActivity viewPagerActivity, final Bundle savedInstanceState) {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        StartAppAd.showSplash(viewPagerActivity, savedInstanceState);
                    }
                }, 0, 30, TimeUnit.MINUTES);
    }


    private void populateDataFromServer() {
        String response = Paper.book().read(Integer.toString(urlToHit.hashCode()));
        if (response == null || response.isEmpty()) {
            getData(AppConstants.WALLPAPER_LIST_REQUEST, this, ONLY_CACHE, urlToHit);
        } else {
            handleResponse(response);
        }
    }

    @Override
    public void onHttpResponse(int requestType, String response) {
        if (requestType == AppConstants.WALLPAPER_LIST_REQUEST) {
            handleResponse(response);
        }
    }

    private void handleResponse(String response) {
        ImageListDTO imageListDTO = GsonUtils.getInstance().deserializeJSON(response, ImageListDTO.class);
        url_list = imageListDTO.getImageList();
        adapter.reloadList(url_list);

        //Hide loading panel
        rl_loading.setVisibility(GONE);

        //Write data to paperDb.
        Paper.book().write(Integer.toString(urlToHit.hashCode()), response);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_share) {
            adapter.shareImage();
        }

        if (v.getId() == R.id.tv_set_wallpaper) {
            adapter.setWallpaper();
            Toast.makeText(this, "Please wait while we set wallpaper for you...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}
