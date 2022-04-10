package com.sharkBytesLab.nocomusic.Model;

public class ImageModel {

    private wallpaperModel src;

    public ImageModel(wallpaperModel src) {
        this.src = src;
    }

    public wallpaperModel getSrc() {
        return src;
    }

    public void setSrc(wallpaperModel src) {
        this.src = src;
    }
}
