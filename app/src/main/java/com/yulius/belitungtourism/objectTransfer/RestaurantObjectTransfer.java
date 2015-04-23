package com.yulius.belitungtourism.objectTransfer;

import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.response.RestaurantListResponseData;
import com.yulius.belitungtourism.response.RestaurantNearbyPoiResponseData;

import java.util.ArrayList;

public class RestaurantObjectTransfer {
    public static RestaurantObjectTransfer instance;
    public int totalNight;
    public int maxBudget;
    public int minBudget;
    public RestaurantListResponseData mRestaurantListResponseData;
    public RestaurantNearbyPoiResponseData mRestaurantNearbyPoiResponseData;
    public ArrayList<Poi> mPoiResultList;

    public static RestaurantObjectTransfer getInstance(){
        if(instance == null){
            instance = new RestaurantObjectTransfer();
        }
        return instance;
    }
}
