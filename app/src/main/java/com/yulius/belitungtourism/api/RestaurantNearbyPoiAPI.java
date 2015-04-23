package com.yulius.belitungtourism.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yulius.belitungtourism.BuildConfig;
import com.yulius.belitungtourism.api.base.BaseAPI;
import com.yulius.belitungtourism.api.base.BaseRequest;
import com.yulius.belitungtourism.response.RestaurantNearbyPoiResponseData;

public class RestaurantNearbyPoiAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public RestaurantNearbyPoiAPI(Context context){
        super(context);

        TAG = "RestaurantNearbyPoiAPI";
    }

    public void requestRestaurantNearbyPoiList(){
        String url = API_RESTAURANT_NEARBY_POI_URL;
        Log.v("TEST Request (RestaurantNearbyPoiAPI)", "request restaurant nearby poi");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (RestaurantNearbyPoiAPI)", response);
                }

                Gson gson = new Gson();
                RestaurantNearbyPoiResponseData restaurantNearbyPoiResponseData = null;

                try {
                    restaurantNearbyPoiResponseData = gson.fromJson(response, RestaurantNearbyPoiResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in RestaurantNearbyPoiAPI, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(restaurantNearbyPoiResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (RestaurantNearbyPoiAPI)", "error : " + volleyError.getMessage());
                }
            }
        });

        stringRequest.setTag(TAG);
        mRequestQueue.cancelAll(TAG);

        mRequestQueue.add(stringRequest);

    }

    public void setOnResponseListener(OnResponseListener onResponseListener){
        super.setOnResponseListener(onResponseListener);
        mOnResponseListener = onResponseListener;
    }

    public interface OnResponseListener extends OnBaseResponseListener{
        public void onRequestSuccess(RestaurantNearbyPoiResponseData restaurantNearbyPoiResponseData);
    }
}
