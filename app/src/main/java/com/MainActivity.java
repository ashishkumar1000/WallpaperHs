package com;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.model.WallpaperCategory;
import com.model.WallpaperModel;
import com.util.AppConstants;
import com.util.GsonUtils;
import com.util.SharedPrefUtils;
import com.wallpaperhs.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;

import static com.util.AppConstants.WALLPAPER_CATEGORY_DATA;
import static com.util.SharedPrefUtils.Key.LAST_NETWORK_INFO_API_SUCCESS_TIMESTAMP;

public class MainActivity extends BaseActivity {

    public static final String URL_FOR_IMAGE_LIST = "url_for_image_list";
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<WallpaperCategory> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new AlbumsAdapter(this, categoryList);

        prepareAlbums();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

        String response = Paper.book().read(WALLPAPER_CATEGORY_DATA);
        if (response == null || response.isEmpty()) {
            getData(AppConstants.WALLPAPER_CATEGORY_REQUEST, this, ONLY_CACHE, null);
        } else {
            handleResponse(response);
            getData(AppConstants.WALLPAPER_CATEGORY_REQUEST, this, ONLY_CACHE, null);
        }
    }


    @Override
    public void onHttpResponse(int requestType, String response) {
        if (requestType == AppConstants.WALLPAPER_CATEGORY_REQUEST) {
            handleResponse(response);
        }
    }

    private void handleResponse(String response) {
        WallpaperModel wallpaperModel = GsonUtils.getInstance().deserializeJSON(response, WallpaperModel.class);
        categoryList = wallpaperModel.getWallpaperCategory();
        adapter.reloadList(categoryList);

        //Save time stamp in shared Preferences
        SharedPrefUtils.getInstance().put(LAST_NETWORK_INFO_API_SUCCESS_TIMESTAMP, new Date().getTime());

        //Save data in Paper Db.
        Paper.book().write(WALLPAPER_CATEGORY_DATA, response);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void openViewPager(String imgListUrl) {
        Intent intent = new Intent(this, ViewPagerActivity.class);
        intent.putExtra(URL_FOR_IMAGE_LIST, imgListUrl);
        startActivity(intent);
    }
}