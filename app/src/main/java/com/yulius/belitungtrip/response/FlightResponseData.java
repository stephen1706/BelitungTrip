package com.yulius.belitungtrip.response;

public class FlightResponseData {
    public Entry[] entries;

    public static class Entry {
        public String flightId;
        public String flightName;
        public String flightLogo;
        public String flightLink;
    }
}
