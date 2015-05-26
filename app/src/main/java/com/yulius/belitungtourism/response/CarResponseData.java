package com.yulius.belitungtourism.response;

public class CarResponseData {
    public Entry[] entries;

    public static class Entry {
        public String carId;
        public String carName;
        public int carPrice;
        public int carCapacity;

    }
}
