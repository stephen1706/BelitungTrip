package com.yulius.belitungtrip.response;

public class HotelListResponseData {
    public Entry[] entries;

    public static class Entry {
        public String hotelId;
        public String hotelName;
        public String hotelLocation;
    }
}
