package com.goixeomdriver.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 7/27/2017.
 */

public class NextBooking {
    @SerializedName("t_id")
    int idTrip;
    @SerializedName("u_id")
    int idUser;

    public int getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
