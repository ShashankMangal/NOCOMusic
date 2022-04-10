package com.sharkBytesLab.nocomusic.Model;

public class HistoryModel {

    String date, detail, paytm;
    int amount;


    public HistoryModel(String date, String detail, String paytm, int amount) {
        this.date = date;
        this.detail = detail;
        this.paytm = paytm;
        this.amount = amount;
    }

    public HistoryModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
