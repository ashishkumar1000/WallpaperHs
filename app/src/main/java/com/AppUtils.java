package com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;


public class AppUtils extends Application {
    private static final int MIN_DAYS_FOR_BOOK_REQUEST = 3;
    private static AppUtils sInstance;
    private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


        Paper.init(this);
    }

    public Context getContext() {
        return appInstance.getApplicationContext();
    }

    public static synchronized AppUtils getInstance() {
        if (sInstance == null) {
            sInstance = new AppUtils();
        }
        return sInstance;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Bitmap getCompressedBackground(int resId, int width, int height, Bitmap.Config config) {
        Bitmap bitmap = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = config;
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(AppUtils.getInstance().getApplicationContext().getResources(),
                resId, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image.
        int scaleFactor = (int) Math.max(1.0, Math.min((double) photoW / (double) width, (double) photoH / (double) height));
        scaleFactor = (int) Math.pow(2.0, Math.floor(Math.log((double) scaleFactor) / Math.log(2.0)));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        do {
            try {
                scaleFactor *= 2;
                bitmap = BitmapFactory.decodeResource(AppUtils.getInstance().getApplicationContext().getResources(), resId, bmOptions);
            } catch (OutOfMemoryError e) {
                bmOptions.inSampleSize = scaleFactor;
            }
        }
        while (bitmap == null && scaleFactor <= 256);

        return bitmap;

    }

    /**
     * This method checks if dialog fragment is active or not
     *
     * @param frag
     * @return
     */
    public static boolean isDialogFragmentUIActive(DialogFragment frag) {
        return (frag != null && frag.getActivity() != null && !frag.getActivity().isFinishing() && frag.getDialog() != null &&
                frag.getDialog().isShowing() && frag.isResumed());
    }


    /**
     * This method dismiss a dialog fragment.
     *
     * @param activity
     * @param dialogFragment
     */
    public static void dismissDialogFragment(Activity activity, DialogFragment dialogFragment) {
        if (AppUtils.isDialogFragmentUIActive(dialogFragment)
                && null != activity && activity.isFinishing()) {
            dialogFragment.dismissAllowingStateLoss();
        }
    }

    public String getDeviceId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}