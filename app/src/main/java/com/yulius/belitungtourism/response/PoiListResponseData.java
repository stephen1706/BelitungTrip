package com.yulius.belitungtourism.response;

public class PoiListResponseData {
    public Entry[] entries;

    public static class Entry {
        public int poiId;
        public String poiName;
        public String poiAddress;
        public int poiPrice;
        public int poiRating;
    }
}
