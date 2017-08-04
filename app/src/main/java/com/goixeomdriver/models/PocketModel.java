package com.goixeomdriver.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 7/3/2017.
 */

public class PocketModel {

    @SerializedName("day")
    private Day day;
    @SerializedName("week")
    private Week week;
    @SerializedName("month")
    private Month month;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public static class Day {
        @SerializedName("trip")
        private int trip;
        @SerializedName("distance")
        private String distance;
        @SerializedName("cost")
        private int cost;
        @SerializedName("total")
        private int total;

        public int getTrip() {
            return trip;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setTrip(int trip) {
            this.trip = trip;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }

    public static class Week {
        @SerializedName("trip")
        private int trip;
        @SerializedName("distance")
        private String distance;
        @SerializedName("cost")
        private int cost;
        @SerializedName("total")
        private int total;
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("endDate")
        private String endDate;

        public int getTrip() {
            return trip;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setTrip(int trip) {
            this.trip = trip;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
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
    }

    public static class Month {
        @SerializedName("trip")
        private int trip;
        @SerializedName("distance")
        private String distance;
        @SerializedName("cost")
        private int cost;
        @SerializedName("total")
        private int total;
        public int getTrip() {
            return trip;
        }
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("endDate")
        private String endDate;
        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
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
        public void setTrip(int trip) {
            this.trip = trip;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }
}
