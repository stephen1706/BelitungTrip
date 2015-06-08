package com.yulius.belitungtourism.realm;

import io.realm.RealmObject;

public class Hotel extends RealmObject {
    private int hotelId;
    private String hotelName;
    private int hotelPrice;
    private int hotelRating;
    private int hotelStar;
    private String hotelImageUrl;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(int hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public int getHotelRating() {
        return hotelRating;
    }

    public void setHotelRating(int hotelRating) {
        this.hotelRating = hotelRating;
    }

    public int getHotelStar()
    {
        return hotelStar;
    }

    public void setHotelStar(int hotelStar)
    {
        this.hotelStar = hotelStar;
    }

    public String getHotelImageUrl()
    {
        return hotelImageUrl;
    }

    public void setHotelImageUrl(String hotelImageUrl)
    {
        this.hotelImageUrl = hotelImageUrl;
    }
}
