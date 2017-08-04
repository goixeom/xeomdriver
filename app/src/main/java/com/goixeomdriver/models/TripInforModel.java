package com.goixeomdriver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/20/2017.
 */

public class TripInforModel implements Parcelable {

    @SerializedName("user")
    private mUser user;
    @SerializedName("trip-info")
    private Trip_info trip_info;

    public mUser getUser() {
        return user;
    }

    public void setUser(mUser user) {
        this.user = user;
    }

    public Trip_info getTrip_info() {
        return trip_info;
    }

    public void setTrip_info(Trip_info trip_info) {
        this.trip_info = trip_info;
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
        @SerializedName("time_start")
        private String time_start;
        @SerializedName("t_id")
        private int idTrip;
        @SerializedName("id-code")
        private int idCode;
        @SerializedName("lat_from")
        private double latFrom;
        @SerializedName("lng_from")
        private double lngFrom;
        @SerializedName("lat_to")
        private double latTo;
        @SerializedName("lng_to")
        private double lngTo;
        @SerializedName("distance")
        private double distance;
        @SerializedName("promotion_value")
        private int promotionValue;

        @SerializedName("status")
        private int status;
        public double getDistance() {
            return distance;
        }

        public int getPromotionValue() {
            return promotionValue;
        }

        public void setPromotionValue(int promotionValue) {
            this.promotionValue = promotionValue;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getLatFrom() {
            return latFrom;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setLatFrom(double latFrom) {
            this.latFrom = latFrom;
        }

        public double getLngFrom() {
            return lngFrom;
        }

        public void setLngFrom(double lngFrom) {
            this.lngFrom = lngFrom;
        }

        public double getLatTo() {
            return latTo;
        }

        public void setLatTo(double latTo) {
            this.latTo = latTo;
        }

        public double getLngTo() {
            return lngTo;
        }

        public void setLngTo(double lngTo) {
            this.lngTo = lngTo;
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

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public int getIdCode() {
            return idCode;
        }

        public void setIdCode(int idCode) {
            this.idCode = idCode;
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
            dest.writeString(this.time_start);
            dest.writeInt(this.idTrip);
            dest.writeInt(this.idCode);
            dest.writeDouble(this.latFrom);
            dest.writeDouble(this.lngFrom);
            dest.writeDouble(this.latTo);
            dest.writeDouble(this.lngTo);
            dest.writeDouble(this.distance);
        }

        protected Trip_info(Parcel in) {
            this.price = in.readInt();
            this.start = in.readString();
            this.end = in.readString();
            this.time_start = in.readString();
            this.idTrip = in.readInt();
            this.idCode = in.readInt();
            this.latFrom = in.readDouble();
            this.lngFrom = in.readDouble();
            this.latTo = in.readDouble();
            this.lngTo = in.readDouble();
            this.distance = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.trip_info, flags);
    }

    public TripInforModel() {
    }

    protected TripInforModel(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.trip_info = in.readParcelable(Trip_info.class.getClassLoader());
    }

    public static final Creator<TripInforModel> CREATOR = new Creator<TripInforModel>() {
        @Override
        public TripInforModel createFromParcel(Parcel source) {
            return new TripInforModel(source);
        }

        @Override
        public TripInforModel[] newArray(int size) {
            return new TripInforModel[size];
        }
    };
}
