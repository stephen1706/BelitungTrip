package com.yulius.belitungtourism.response;

public class RestaurantListResponseData {
    public Entry[] entries;

    public static class Entry {
        public int restaurantId;
        public String restaurantName;
        public String restaurantAddress;
        public int restaurantPrice;
        public int restaurantRating;
        public int restaurantType;
    }
}
