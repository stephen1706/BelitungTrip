package com.yulius.belitungtourism.realm;

import io.realm.RealmObject;

public class Poi extends RealmObject {
    private int poiId;
    private String poiName;
    private int poiPrice;
    private int poiRating;

    public int getPoiId() {
        return poiId;
    }

    public void setPoiId(int poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public int getPoiPrice() {
        return poiPrice;
    }

    public void setPoiPrice(int poiPrice) {
        this.poiPrice = poiPrice;
    }

    public int getPoiRating() {
        return poiRating;
    }

    public void setPoiRating(int poiRating) {
        this.poiRating = poiRating;
    }
}
