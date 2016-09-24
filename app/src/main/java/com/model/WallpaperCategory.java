
package com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WallpaperCategory {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("img_list_url")
    @Expose
    private String imgListUrl;

    public WallpaperCategory(String name, String subtitle, String imgUrl, String imgListUrl) {
        this.name = name;
        this.subtitle = subtitle;
        this.imgListUrl = imgListUrl;
        this.imgUrl = imgUrl;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle The subtitle
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * @return The imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl The img_url
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return The imgListUrl
     */
    public String getImgListUrl() {
        return imgListUrl;
    }

    /**
     * @param imgListUrl The img_list_url
     */
    public void setImgListUrl(String imgListUrl) {
        this.imgListUrl = imgListUrl;
    }

}
