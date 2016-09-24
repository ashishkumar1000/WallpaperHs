package com.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ImageListDTO {

    @SerializedName("image_list")
    @Expose
    private List<String> imageList = new ArrayList<>();

    /**
     * @return The imageList
     */
    public List<String> getImageList() {
        return imageList;
    }

    /**
     * @param imageList The image_list
     */
    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

}