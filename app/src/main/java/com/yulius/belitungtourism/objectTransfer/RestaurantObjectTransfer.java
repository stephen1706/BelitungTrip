package com.yulius.belitungtourism.objectTransfer;

import com.yulius.belitungtourism.response.RestaurantListResponseData;

public class RestaurantObjectTransfer {
    public static RestaurantObjectTransfer instance;
    public int totalNight;
    public int maxBudget;
    public RestaurantListResponseData mRestaurantListResponseData;

    public static RestaurantObjectTransfer getInstance(){
        if(instance == null){
            instance = new RestaurantObjectTransfer();
        }
        return instance;
    }
}
