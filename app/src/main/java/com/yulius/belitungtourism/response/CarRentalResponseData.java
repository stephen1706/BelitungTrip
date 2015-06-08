package com.yulius.belitungtourism.response;


public class CarRentalResponseData {
    public Entry[] entries;

    public static class Entry {
        public String carId;
        public String carName;
        public String carLogo;
        public String carLink;
    }
}
