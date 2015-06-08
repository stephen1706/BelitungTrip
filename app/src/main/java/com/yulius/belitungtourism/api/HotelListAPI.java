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
import com.yulius.belitungtourism.response.HotelListResponseData;

public class HotelListAPI extends BaseAPI {

    private OnResponseListener mOnResponseListener;

    public HotelListAPI(Context context){
        super(context);

        TAG = "HotelListAPI";
    }

    public void requestHotelList(){
        String url = API_HOTEL_LIST_URL;
        Log.v("TEST Request (HotelListAPI)", "request hotel list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (HotelListAPI)", response);
                }
                Log.d("response", response);

                Gson gson = new Gson();
                HotelListResponseData hotelListResponseData = null;

                try {
                    hotelListResponseData = gson.fromJson(response, HotelListResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in hotel list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(hotelListResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (HotelListAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(HotelListResponseData hotelListResponseData);
    }
}
