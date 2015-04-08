package com.yulius.belitungtrip.response;

public class SouvenirListResponseData {
    public Entry[] entries;

    public static class Entry {
        public int souvenirId;
        public String souvenirName;
        public String souvenirLocation;
        public int souvenirRating;
        public int souvenirPrice;
    }
}
