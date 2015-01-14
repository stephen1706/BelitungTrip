package com.yulius.belitungtrip.response;

public class PoiListResponseData {
    public Entry[] entries;

    public static class Entry {
        public String poiId;
        public String poiName;
        public String poiAddress;
    }
}
