package com.adapter;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.AppUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wallpaperhs.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class ViewPagerAdapter extends PagerAdapter {

    private static List<String> url_list = new ArrayList<>();
    //= AppUtils.getInstance().getContext().getResources().getStringArray(R.array.url_list);

    private PhotoView photoView;

    public ViewPagerAdapter(List<String> url_list) {
        Collections.shuffle(url_list);
        this.url_list = url_list;
    }

    @Override
    public int getCount() {
        return url_list.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(container.getContext());
        this.photoView = photoView;
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
        Collections.shuffle(url_list);
        this.url_list = url_list;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public void shareImage() {
        try {
            Bitmap bmp = photoView.getVisibleRectangleBitmap();
            File tmpFile = File.createTempFile("photoview", ".png", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            FileOutputStream out = new FileOutputStream(tmpFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tmpFile));
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppUtils.getInstance().getContext().startActivity(share);
            Toast.makeText(AppUtils.getInstance().getContext(), String.format("Extracted into: %s", tmpFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            t.printStackTrace();
            Toast.makeText(AppUtils.getInstance().getContext(), "Error occured while extracting bitmap", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getBitMap() {
        Bitmap bmp = null;
        try {
            bmp = photoView.getVisibleRectangleBitmap();
            File tmpFile = File.createTempFile("photoview", ".png", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            FileOutputStream out = new FileOutputStream(tmpFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
            Toast.makeText(AppUtils.getInstance().getContext(), "Error occured while extracting bitmap", Toast.LENGTH_SHORT).show();
        }
        return bmp;
    }


    public void setWallpaper() {
        DisplayMetrics displayMetrics = AppUtils.getInstance().getContext().getResources().getDisplayMetrics();

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(AppUtils.getInstance().getContext());

        try {
            Bitmap bitmap = getBitMap();
            if (bitmap != null) {
                wallpaperManager.suggestDesiredDimensions(width, height);
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(AppUtils.getInstance().getContext(), "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppUtils.getInstance().getContext(), "Error occured while setting Wallpaper", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}