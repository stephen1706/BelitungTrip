package com.yulius.belitungtrip.response;

public class HotelDetailResponseData {
    public String hotelName;
    public String hotelLocation;
    public String hotelStars;
    public String hotelWebsite;
    public String hotelLatitude;
    public String hotelLongitude;
    public String hotelAddress;
    public String photosphere;
    public Asset[] assets;

    public static class Asset {
        public String url;
    }
}
