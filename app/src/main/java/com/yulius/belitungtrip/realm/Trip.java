package com.yulius.belitungtrip.realm;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Trip extends RealmObject {
    @PrimaryKey
    private String tripName;
    private int numGuests;
    private int totalNight;
    private RealmList<Restaurant> restaurants;
    private RealmList<Poi> pois;
    private Hotel hotel;
    private Souvenir souvenir;

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
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

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public int getTotalNight() {
        return totalNight;
    }

    public void setTotalNight(int totalNight) {
        this.totalNight = totalNight;
    }

    public Souvenir getSouvenir() {
        return souvenir;
    }

    public void setSouvenir(Souvenir souvenir) {
        this.souvenir = souvenir;
    }

}
