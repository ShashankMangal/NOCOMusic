package com.sharkBytesLab.nocomusic.Model;

public class AppStatsModel {

    int num;
    int songs;
    int subs;

    public AppStatsModel(int num, int songs, int subs) {
        this.num = num;
        this.songs = songs;
        this.subs = subs;
    }

    public AppStatsModel() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSongs() {
        return songs;
    }

    public void setSongs(int songs) {
        this.songs = songs;
    }

    public int getSubs() {
        return subs;
    }

    public void setSubs(int subs) {
        this.subs = subs;
    }
}
