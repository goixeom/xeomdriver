package com.goixeomdriver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/14/2017.
 */

public class User implements Parcelable {
    @SerializedName("booking")
    private int isInBooking;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("status")
    private int status;
    @SerializedName("type")
    private int type;
    @SerializedName("active")
    private int active;
    @SerializedName("insurance")
    private int insurance;
    @SerializedName("cmnd")
    private int cmnd;
    @SerializedName("family")
    private int family;
    @SerializedName("license")
    private int license;
    @SerializedName("register")
    private int register;
    @SerializedName("rate")
    private int rate;
    @SerializedName("vote")
    private double vote;
    @SerializedName("link")
    private String link;
    @SerializedName("number")
    private String number;
    @SerializedName("click")
    private int clicked;
    @SerializedName("type_vote")
    private int type_vote;
    @SerializedName("number_vote")
    private int number_vote;
    public int getRate() {
        return rate;
    }
    @SerializedName("type_driver")
    private int type_driver;
    public int getType_vote() {
        return type_vote;
    }

    public int getType_driver() {
        return type_driver;
    }

    public void setType_driver(int type_driver) {
        this.type_driver = type_driver;
    }

    public void setType_vote(int type_vote) {
        this.type_vote = type_vote;
    }

    public int getNumber_vote() {
        return number_vote;
    }

    public void setNumber_vote(int number_vote) {
        this.number_vote = number_vote;
    }

    public int getClicked() {
        return clicked;
    }

    public int getIsInBooking() {
        return isInBooking;
    }

    public void setIsInBooking(int isInBooking) {
        this.isInBooking = isInBooking;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getCmnd() {
        return cmnd;
    }

    public void setCmnd(int cmnd) {
        this.cmnd = cmnd;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isInBooking);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.avatar);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
        dest.writeInt(this.active);
        dest.writeInt(this.insurance);
        dest.writeInt(this.cmnd);
        dest.writeInt(this.family);
        dest.writeInt(this.license);
        dest.writeInt(this.register);
        dest.writeInt(this.rate);
        dest.writeDouble(this.vote);
        dest.writeString(this.link);
        dest.writeString(this.number);
        dest.writeInt(this.clicked);
        dest.writeInt(this.type_vote);
        dest.writeInt(this.number_vote);
        dest.writeInt(this.type_driver);
    }

    protected User(Parcel in) {
        this.isInBooking = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.avatar = in.readString();
        this.status = in.readInt();
        this.type = in.readInt();
        this.active = in.readInt();
        this.insurance = in.readInt();
        this.cmnd = in.readInt();
        this.family = in.readInt();
        this.license = in.readInt();
        this.register = in.readInt();
        this.rate = in.readInt();
        this.vote = in.readDouble();
        this.link = in.readString();
        this.number = in.readString();
        this.clicked = in.readInt();
        this.type_vote = in.readInt();
        this.number_vote = in.readInt();
        this.type_driver = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
