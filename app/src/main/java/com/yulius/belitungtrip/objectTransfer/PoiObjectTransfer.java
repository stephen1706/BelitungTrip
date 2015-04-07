package com.yulius.belitungtrip.objectTransfer;

import com.yulius.belitungtrip.response.PoiListResponseData;

public class PoiObjectTransfer {
    public static PoiObjectTransfer instance;
    public int totalNight;
    public int maxBudget;
    public PoiListResponseData mPoiListResponseData;

    public static PoiObjectTransfer getInstance(){
        if(instance == null){
            instance = new PoiObjectTransfer();
        }
        return instance;
    }
}
