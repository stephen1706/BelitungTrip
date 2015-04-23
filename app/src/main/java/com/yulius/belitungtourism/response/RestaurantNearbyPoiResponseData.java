package com.yulius.belitungtourism.response;

public class RestaurantNearbyPoiResponseData {
    public Entry[] entries;

    public static class Entry {
        public int poiId;
        public int restaurantId;
    }
}
