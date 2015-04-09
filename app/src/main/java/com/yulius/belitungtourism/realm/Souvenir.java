package com.yulius.belitungtourism.realm;

import io.realm.RealmObject;

public class Souvenir extends RealmObject {
    private int souvenirId;
    private String souvenirName;
    private int souvenirPrice;
    private int souvenirRating;

    public int getSouvenirId() {
        return souvenirId;
    }

    public void setSouvenirId(int souvenirId) {
        this.souvenirId = souvenirId;
    }

    public String getSouvenirName() {
        return souvenirName;
    }

    public void setSouvenirName(String souvenirName) {
        this.souvenirName = souvenirName;
    }

    public int getSouvenirPrice() {
        return souvenirPrice;
    }

    public void setSouvenirPrice(int souvenirPrice) {
        this.souvenirPrice = souvenirPrice;
    }

    public int getSouvenirRating() {
        return souvenirRating;
    }

    public void setSouvenirRating(int souvenirRating) {
        this.souvenirRating = souvenirRating;
    }
}
