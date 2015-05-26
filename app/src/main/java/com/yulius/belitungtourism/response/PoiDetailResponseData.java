package com.yulius.belitungtourism.response;

public class PoiDetailResponseData {
    public String poiName;
    public String poiWebsite;
    public String poiLatitude;
    public String poiLongitude;
    public String poiAddress;
    public String poiTelephone;
    public int poiPrice;
    public String poiRating;
    public String photosphere;
    public Asset[] assets;

    public static class Asset {
        public String url;
    }
}