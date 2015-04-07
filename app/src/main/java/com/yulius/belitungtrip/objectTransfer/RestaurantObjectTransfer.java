package com.yulius.belitungtrip.objectTransfer;

import com.yulius.belitungtrip.response.RestaurantListResponseData;

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
