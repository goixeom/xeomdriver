package com.goixeomdriver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/20/2017.
 */

public class DetailTripModel implements Parcelable {

    @SerializedName("driver")
    private Driver driver;
    @SerializedName("trip-info")
    private Trip_info trip_info;
    @SerializedName("user")
    private User user;
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Trip_info getTrip_info() {
        return trip_info;
    }

    public void setTrip_info(Trip_info trip_info) {
        this.trip_info = trip_info;
    }

    public static class Driver implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("vote")
        private int vote;
        @SerializedName("number")
        private String number;
        @SerializedName("model")
        private String model;
        @SerializedName("phone")
        private String phone;
        @SerializedName("brand")
        private String brand;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getVote() {
            return vote;
        }

        public void setVote(int vote) {
            this.vote = vote;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Driver() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.avatar);
            dest.writeInt(this.vote);
            dest.writeString(this.number);
            dest.writeString(this.model);
            dest.writeString(this.phone);
            dest.writeString(this.brand);
        }

        protected Driver(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.avatar = in.readString();
            this.vote = in.readInt();
            this.number = in.readString();
            this.model = in.readString();
            this.phone = in.readString();
            this.brand = in.readString();
        }

        public static final Creator<Driver> CREATOR = new Creator<Driver>() {
            @Override
            public Driver createFromParcel(Parcel source) {
                return new Driver(source);
            }

            @Override
            public Driver[] newArray(int size) {
                return new Driver[size];
            }
        };
    }
    public static class mUser implements Parcelable {

        @SerializedName("u_id")
        private int u_id;
        @SerializedName("name")
        private String name;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("phone")
        private String phone;
        @SerializedName("type")
        private int type;
        @SerializedName("link")
        private String linkType;
        public int getU_id() {
            return u_id;
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        public void setU_id(int u_id) {
            this.u_id = u_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public mUser() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.u_id);
            dest.writeString(this.name);
            dest.writeString(this.avatar);
            dest.writeString(this.phone);
            dest.writeInt(this.type);
            dest.writeString(this.linkType);
        }

        protected mUser(Parcel in) {
            this.u_id = in.readInt();
            this.name = in.readString();
            this.avatar = in.readString();
            this.phone = in.readString();
            this.type = in.readInt();
            this.linkType = in.readString();
        }

        public static final Creator<mUser> CREATOR = new Creator<mUser>() {
            @Override
            public mUser createFromParcel(Parcel source) {
                return new mUser(source);
            }

            @Override
            public mUser[] newArray(int size) {
                return new mUser[size];
            }
        };
    }
    public static class Trip_info implements Parcelable {
        @SerializedName("price")
        private int price;
        @SerializedName("start")
        private String start;
        @SerializedName("end")
        private String end;
        @SerializedName("id-code")
        private int idCode;
        @SerializedName("t_id")
        private int idTrip;
        @SerializedName("status")
        private int status;
        @SerializedName("vote")
        private int vote;
        @SerializedName("date")
        private String date;
        @SerializedName("code")
        private String code;
        @SerializedName("lat_to")
        private double lat_to;
        @SerializedName("lat_from")
        private double lat_from;
        @SerializedName("lng_to")
        private double lng_to;
        @SerializedName("lng_from")
        private double lng_from;
        @SerializedName("static_map")
        private String staticmap;
        public int getIdCode() {
            return idCode;
        }

        public String getStaticmap() {
            return staticmap;
        }

        public void setStaticmap(String staticmap) {
            this.staticmap = staticmap;
        }

        public double getLat_to() {
            return lat_to;
        }

        public void setLat_to(double lat_to) {
            this.lat_to = lat_to;
        }

        public double getLat_from() {
            return lat_from;
        }

        public void setLat_from(double lat_from) {
            this.lat_from = lat_from;
        }

        public double getLng_to() {
            return lng_to;
        }

        public void setLng_to(double lng_to) {
            this.lng_to = lng_to;
        }

        public double getLng_from() {
            return lng_from;
        }

        public void setLng_from(double lng_from) {
            this.lng_from = lng_from;
        }

        public int getVote() {
            return vote;
        }

        public void setVote(int vote) {
            this.vote = vote;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setIdCode(int idCode) {
            this.idCode = idCode;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public int getIdTrip() {
            return idTrip;
        }

        public void setIdTrip(int idTrip) {
            this.idTrip = idTrip;
        }

        public Trip_info() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.price);
            dest.writeString(this.start);
            dest.writeString(this.end);
            dest.writeInt(this.idCode);
            dest.writeInt(this.idTrip);
            dest.writeInt(this.status);
            dest.writeInt(this.vote);
            dest.writeString(this.date);
            dest.writeString(this.code);
            dest.writeDouble(this.lat_to);
            dest.writeDouble(this.lat_from);
            dest.writeDouble(this.lng_to);
            dest.writeDouble(this.lng_from);
            dest.writeString(this.staticmap);
        }

        protected Trip_info(Parcel in) {
            this.price = in.readInt();
            this.start = in.readString();
            this.end = in.readString();
            this.idCode = in.readInt();
            this.idTrip = in.readInt();
            this.status = in.readInt();
            this.vote = in.readInt();
            this.date = in.readString();
            this.code = in.readString();
            this.lat_to = in.readDouble();
            this.lat_from = in.readDouble();
            this.lng_to = in.readDouble();
            this.lng_from = in.readDouble();
            this.staticmap = in.readString();
        }

        public static final Creator<Trip_info> CREATOR = new Creator<Trip_info>() {
            @Override
            public Trip_info createFromParcel(Parcel source) {
                return new Trip_info(source);
            }

            @Override
            public Trip_info[] newArray(int size) {
                return new Trip_info[size];
            }
        };
    }

    public DetailTripModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.driver, flags);
        dest.writeParcelable(this.trip_info, flags);
        dest.writeParcelable(this.user, flags);
    }

    protected DetailTripModel(Parcel in) {
        this.driver = in.readParcelable(Driver.class.getClassLoader());
        this.trip_info = in.readParcelable(Trip_info.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<DetailTripModel> CREATOR = new Creator<DetailTripModel>() {
        @Override
        public DetailTripModel createFromParcel(Parcel source) {
            return new DetailTripModel(source);
        }

        @Override
        public DetailTripModel[] newArray(int size) {
            return new DetailTripModel[size];
        }
    };
}
