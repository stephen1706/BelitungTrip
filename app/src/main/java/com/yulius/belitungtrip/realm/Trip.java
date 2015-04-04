package com.yulius.belitungtrip.realm;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Trip extends RealmObject {
    @PrimaryKey
    private String tripName;
    private int price;
    private int numGuests;
    private RealmList<Restaurant> restaurants;
    private RealmList<Poi> pois;
    private RealmList<Hotel> hotel;

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public RealmList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(RealmList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public RealmList<Poi> getPois() {
        return pois;
    }

    public void setPois(RealmList<Poi> pois) {
        this.pois = pois;
    }

    public RealmList<Hotel> getHotel() {
        return hotel;
    }

    public void setHotel(RealmList<Hotel> hotel) {
        this.hotel = hotel;
    }
}
