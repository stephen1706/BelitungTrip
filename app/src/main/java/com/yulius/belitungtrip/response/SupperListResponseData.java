package com.yulius.belitungtrip.response;

public class SupperListResponseData {
    public Entry[] entries;

    public static class Entry {
        public String supperId;
        public String supperName;
        public String supperLocation;
        public String supperRating;
        public String supperPrice;
    }
}
