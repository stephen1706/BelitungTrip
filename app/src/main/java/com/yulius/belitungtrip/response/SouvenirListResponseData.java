package com.yulius.belitungtrip.response;

public class SouvenirListResponseData {
    public Entry[] entries;

    public static class Entry {
        public String souvenirId;
        public String souvenirName;
        public String souvenirLocation;
        public String souvenirRating;
        public String souvenirPrice;
    }
}
