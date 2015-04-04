package com.yulius.belitungtrip.response;

public class RestaurantListResponseData {
    public Entry[] entries;

    public static class Entry {
        public String restaurantId;
        public String restaurantName;
        public String restaurantAddress;
        public String restaurantPrice;
        public String restaurantRating;
        public String restaurantType;
    }
}
