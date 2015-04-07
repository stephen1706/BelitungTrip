package com.yulius.belitungtrip.response;

public class HotelListResponseData {
    public Entry[] entries;

    public static class Entry {
        public int hotelId;
        public String hotelName;
        public String hotelLocation;
        public int hotelRating;
        public int hotelPrice;
    }
}
