package com.yulius.belitungtourism.response;

public class SouvenirListResponseData {
    public Entry[] entries;

    public static class Entry {
        public int souvenirId;
        public String souvenirName;
        public String souvenirAddress;
        public int souvenirRating;
        public int souvenirPrice;
    }
}
