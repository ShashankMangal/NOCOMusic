package com.sharkBytesLab.nocomusic.Model;

public class PromotionWebViewModel {

    String channelUrl;
    String promotionBy;
    String channelName;
    String startDate, endDate;

    public PromotionWebViewModel(){}

    public PromotionWebViewModel(String channelUrl, String promotionBy, String channelName, String startDate, String endDate) {
        this.channelUrl = channelUrl;
        this.promotionBy = promotionBy;
        this.channelName = channelName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getPromotionBy() {
        return promotionBy;
    }

    public void setPromotionBy(String promotionBy) {
        this.promotionBy = promotionBy;
    }
}
