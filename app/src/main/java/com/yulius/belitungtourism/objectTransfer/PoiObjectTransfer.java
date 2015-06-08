package com.yulius.belitungtourism.objectTransfer;

import com.yulius.belitungtourism.response.PoiListResponseData;

public class PoiObjectTransfer {
    public static PoiObjectTransfer instance;
    public int totalNight;
    public int maxBudget;
    public int minBudget;
    public PoiListResponseData mPoiListResponseData;

    public static PoiObjectTransfer getInstance(){
        if(instance == null){
            instance = new PoiObjectTransfer();
        }
        return instance;
    }
}
