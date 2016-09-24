package com.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wallpaperhs.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class ViewPagerAdapter extends PagerAdapter {

    private static List<String> url_list = new ArrayList<>();
    //= AppUtils.getInstance().getContext().getResources().getStringArray(R.array.url_list);

    public ViewPagerAdapter(List<String> url_list) {
        this.url_list = url_list;
    }

    @Override
    public int getCount() {
        return url_list.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(container.getContext());

        Picasso picasso = Picasso.with(container.getContext());
        picasso.setIndicatorsEnabled(false);
        picasso.load(url_list.get(position))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(container.getContext())
                                .load(url_list.get(position))
                                .placeholder(R.drawable.progress_animation)
                                .fit()
                                .centerCrop()
                                .into(photoView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public void reloadList(List<String> url_list) {
        this.url_list = url_list;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}