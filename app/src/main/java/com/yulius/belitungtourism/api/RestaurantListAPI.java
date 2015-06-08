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
import com.yulius.belitungtourism.response.RestaurantListResponseData;


public class RestaurantListAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public RestaurantListAPI(Context context){
        super(context);

        TAG = "RestaurantListAPI";
    }

    public void requestRestaurantList(){
        String url = API_RESTAURANT_LIST_URL;
        Log.v("TEST Request (RestaurantListAPI)", "request restaurant list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (RestaurantListAPI)", response);
                }

                Gson gson = new Gson();
                RestaurantListResponseData restaurantListResponseData = null;

                try {
                    restaurantListResponseData = gson.fromJson(response, RestaurantListResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in restaurant list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(restaurantListResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (RestaurantListAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(RestaurantListResponseData restaurantListResponseData);
    }
}
