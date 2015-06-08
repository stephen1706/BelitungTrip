package com.yulius.belitungtourism.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yulius.belitungtourism.BuildConfig;
import com.yulius.belitungtourism.api.base.BaseAPI;
import com.yulius.belitungtourism.response.BudgetMaxMinResponseData;

import java.util.HashMap;
import java.util.Map;

public class RestaurantMaxMinAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public RestaurantMaxMinAPI(Context context){
        super(context);

        TAG = "RestaurantMaxMinAPI";
    }

    public void requestRestaurantMaxMin(final String days){
        String url = API_RESTAURANT_MAX_MIN_URL;
        Log.v("TEST Request (RestaurantMaxMinAPI)", "request restaurant max min");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (RestaurantMaxMinAPI)", response);
                }

                Gson gson = new Gson();
                BudgetMaxMinResponseData budgetMaxMinResponseData = null;

                try {
                    budgetMaxMinResponseData = gson.fromJson(response, BudgetMaxMinResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in RESTAURANT MAX MIN, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(budgetMaxMinResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (RestaurantMaxMinAPI)", "error : " + volleyError.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("days",days);

                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        stringRequest.setTag(TAG);
        mRequestQueue.cancelAll(TAG);

        mRequestQueue.add(stringRequest);

    }

    public void setOnResponseListener(OnResponseListener onResponseListener){
        super.setOnResponseListener(onResponseListener);
        mOnResponseListener = onResponseListener;
    }

    public interface OnResponseListener extends OnBaseResponseListener{
        public void onRequestSuccess(BudgetMaxMinResponseData budgetMaxMinResponseData);
    }
}
