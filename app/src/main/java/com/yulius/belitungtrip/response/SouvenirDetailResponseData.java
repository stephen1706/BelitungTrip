package com.yulius.belitungtrip.response;

public class SouvenirDetailResponseData {
    public String souvenirName;
    public String souvenirStars;
    public String souvenirWebsite;
    public String souvenirLatitude;
    public String souvenirLongitude;
    public String souvenirAddress;
    public String photosphere;
    public String souvenirTelephone;
    public String souvenirRating;
    public String souvenirPrice;
    public Asset[] assets;

    public static class Asset {
        public String url;
    }
}
