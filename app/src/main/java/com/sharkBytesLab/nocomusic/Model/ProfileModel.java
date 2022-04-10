package com.sharkBytesLab.nocomusic.Model;

public class ProfileModel
{
    private String name, paytm, referCode;
    private int coins;


    public ProfileModel(int coins,String name,   String paytm, String referCode) {
        this.name = name;
        this.coins = coins;
        this.paytm = paytm;
        this.referCode = referCode;
    }

    public ProfileModel()
    {

    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

}
