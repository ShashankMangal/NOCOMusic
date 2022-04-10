package com.sharkBytesLab.nocomusic.Model;

public class PromotionModel {


    String channelName;
    String userUniqueId;
    String status;
    String channelUrl;

    public PromotionModel(){}

    public PromotionModel(String channelName, String userUniqueId, String status, String channelUrl) {
        this.channelName = channelName;
        this.userUniqueId = userUniqueId;
        this.status = status;
        this.channelUrl = channelUrl;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
