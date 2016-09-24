package com;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    private String name;
    private int subTitle;
    private int thumbnail;

    public Album() {
    }

    public Album(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.subTitle = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(int subTitle) {
        this.subTitle = subTitle;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
