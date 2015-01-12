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
import com.yulius.belitungtrip.response.HotelDetailResponseData;

import java.util.HashMap;
import java.util.Map;

public class HotelDetailAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public HotelDetailAPI(Context context){
        super(context);

        TAG = "HotelDetailAPI";
    }
    public void requestHotelDetail(final String hotelId){
        String url = API_HOTEL_DETAIL_URL;
        Log.v("TEST Request (HotelDetailAPI)", "request hotel detail with id : " + hotelId);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (HotelDetailAPI)", response);
                }

                Gson gson = new Gson();
                HotelDetailResponseData hotelDetailResponseData = null;

                try {
                    hotelDetailResponseData = gson.fromJson(response, HotelDetailResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in hotel detail, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(hotelDetailResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (HotelDetailAPI)", "error : " + volleyError.getMessage());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",hotelId);

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
        public void onRequestSuccess(HotelDetailResponseData hotelDetailResponseData);
    }
}
