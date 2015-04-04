package com.yulius.belitungtrip.realm;

import io.realm.RealmObject;

public class Restaurant extends RealmObject {
    private int restaurantId;
    private String restaurantName;
    private int restaurantPrice;
    private int restaurantRating;
    private String nearbyPoiId;

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getRestaurantPrice() {
        return restaurantPrice;
    }

    public void setRestaurantPrice(int restaurantPrice) {
        this.restaurantPrice = restaurantPrice;
    }

    public int getRestaurantRating() {
        return restaurantRating;
    }

    public void setRestaurantRating(int restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public String getNearbyPoiId() {
        return nearbyPoiId;
    }

    public void setNearbyPoiId(String nearbyPoiId) {
        this.nearbyPoiId = nearbyPoiId;
    }
}
