package com.yulius.belitungtourism.response;

public class SupperDetailResponseData {
    public String supperName;
    public String supperStars;
    public String supperWebsite;
    public String supperLatitude;
    public String supperLongitude;
    public String supperAddress;
    public String photosphere;
    public String supperTelephone;
    public String supperRating;
    public String supperPrice;
    public Asset[] assets;

    public static class Asset {
        public String url;
    }
}
