package com.yulius.belitungtourism.realm;

import io.realm.RealmObject;

public class Restaurant extends RealmObject {
    private int restaurantId;
    private String restaurantName;
    private int restaurantPrice;
    private int restaurantRating;
    private int restaurantType;

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

    public int getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(int restaurantType) {
        this.restaurantType = restaurantType;
    }
}
