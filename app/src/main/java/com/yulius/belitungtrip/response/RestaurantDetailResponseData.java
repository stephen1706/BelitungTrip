package com.yulius.belitungtrip.response;

public class RestaurantDetailResponseData {
    public String restaurantName;
    public String restaurantWebsite;
    public String restaurantLatitude;
    public String restaurantLongitude;
    public String restaurantAddress;
    public String restaurantTelephone;
    public String restaurantPrice;
    public String restaurantRating;
    public String restaurantType;
    public String photosphere;
    public Asset[] assets;

    public static class Asset {
        public String url;
    }
}
