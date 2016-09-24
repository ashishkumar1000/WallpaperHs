
package com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WallpaperModel {

    @SerializedName("wallpaper_category")
    @Expose
    private List<WallpaperCategory> wallpaperCategory = new ArrayList<WallpaperCategory>();

    /**
     * 
     * @return
     *     The wallpaperCategory
     */
    public List<WallpaperCategory> getWallpaperCategory() {
        return wallpaperCategory;
    }

    /**
     * 
     * @param wallpaperCategory
     *     The wallpaper_category
     */
    public void setWallpaperCategory(List<WallpaperCategory> wallpaperCategory) {
        this.wallpaperCategory = wallpaperCategory;
    }

}
