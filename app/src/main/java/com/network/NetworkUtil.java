package com.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


import com.AppUtils;
import com.BaseActivity;
import com.model.NetworkDTO;
import com.util.GsonUtils;
import com.util.SharedPrefUtils;

import java.io.IOException;
import java.util.Date;

import io.paperdb.Paper;



public class NetworkUtil extends BaseActivity {
    private static final String SUCCESS = "success";
    private static NetworkUtil sInstance;
    private NetworkDTO networkDTO;


    private NetworkUtil() {
    }

    public static synchronized NetworkUtil getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkUtil();
        }
        return sInstance;
    }

    @Override
    public void onHttpFailure(IOException e) {
        super.onHttpFailure(e);
    }

    @Override
    public void onHttpResponse(int requestType, String response) {
       /* super.onHttpResponse(requestType, response);
        networkDTO = GsonUtils.getInstance().deserializeJSON(response, NetworkDTO.class);
        if (SUCCESS.equalsIgnoreCase(networkDTO.getStatus())) {
            //Write data to local paper db.
            Paper.book().write(NETWORK_INFO_DATA, networkDTO);

            //Log Data to Flurry
            TrackingHelper.logNetworkData(networkDTO);

            //Save success time stamp response.
            SharedPrefUtils.getInstance().put(LAST_NETWORK_INFO_API_SUCCESS_TIMESTAMP, new Date().getTime());
        }*/
    }

    public void populateNetworkInfoInLocalPaperDb() {
        /*Paper.init(AppUtils.getInstance().getContext());
        networkDTO = Paper.book().read(NETWORK_INFO_DATA, null);
        if (null == networkDTO) {
            //Check if network hit is required.
            if (AppUtils.getInstance().isNetworkRequestRequiredForListing(NETWORK_INFO_REQUEST)) {
                //fetch data from network.
                getData(NETWORK_INFO_REQUEST, this, ONLY_CACHE);
            }
        } else {
            //Log Data to Flurry
            TrackingHelper.logNetworkData(networkDTO);
        }*/
    }

    public NetworkDTO getNetworkDTO() {
        return this.networkDTO;
    }

    public String getCountry() {
        if (networkDTO != null) {
            return networkDTO.getCountry();
        } else {
            return "Unknown country";
        }
    }

    public String getNetworkState() {
        StringBuilder networkType = new StringBuilder("");
        Context context = AppUtils.getInstance().getContext();
        networkType.append(isConnected(context) ? "Connected:" : "Disconnected:");

        if (isConnected(context)) {
            networkType.append(isConnectedFast(context) ? "Fast:" : "Slow:");
            networkType.append(isConnectedWifi(context) ? "Wifi:" : "");
            networkType.append(isConnectedMobile(context) ? "MobileData:" : "");
        }
        return networkType.toString();
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = NetworkUtil.getNetworkInfo(context);
        return (info != null && info.isConnected() && NetworkUtil.isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = NetworkUtil.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = NetworkUtil.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = NetworkUtil.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}