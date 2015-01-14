package com.yulius.belitungtrip.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yulius.belitungtrip.BuildConfig;
import com.yulius.belitungtrip.api.base.BaseAPI;
import com.yulius.belitungtrip.response.RestaurantDetailResponseData;

import java.util.HashMap;
import java.util.Map;

public class RestaurantDetailAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public RestaurantDetailAPI(Context context){
        super(context);

        TAG = "RestaurantDetailAPI";
    }
    public void requestRestaurantDetail(final String restaurantId){
        String url = API_RESTAURANT_DETAIL_URL;
        Log.v("TEST Request (RestaurantDetailAPI)", "request restaurant detail with id : " + restaurantId);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (RestaurantDetailAPI)", response);
                }

                Gson gson = new Gson();
                RestaurantDetailResponseData restaurantDetailResponseData = null;

                try {
                    restaurantDetailResponseData = gson.fromJson(response, RestaurantDetailResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in restaurant detail, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(restaurantDetailResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (RestaurantDetailAPI)", "error : " + volleyError.getMessage());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",restaurantId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        mRequestQueue.add(sr);
    }

    public void setOnResponseListener(OnResponseListener onResponseListener){
        super.setOnResponseListener(onResponseListener);
        mOnResponseListener = onResponseListener;
    }

    public interface OnResponseListener extends OnBaseResponseListener{
        public void onRequestSuccess(RestaurantDetailResponseData restaurantDetailResponseData);
    }
}
