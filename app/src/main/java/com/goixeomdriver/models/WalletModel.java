package com.goixeomdriver.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 7/3/2017.
 */

public class WalletModel {

    @SerializedName("account")
    private long account;
    @SerializedName("road")
    private long road;
    @SerializedName("date")
    private String date;

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public long getRoad() {
        return road;
    }

    public void setRoad(long road) {
        this.road = road;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
