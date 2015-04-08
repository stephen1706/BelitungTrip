package com.yulius.belitungtrip.response;

public class HotelDetailResponseData {
    public String hotelName;
    public String hotelStars;
    public String hotelWebsite;
    public String hotelLatitude;
    public String hotelLongitude;
    public String hotelAddress;
    public String photosphere;
    public String hotelTelephone;
    public String hotelRating;
    public int hotelPrice;
    public Asset[] assets;

    public static class Asset {
        public String url;
    }
}
