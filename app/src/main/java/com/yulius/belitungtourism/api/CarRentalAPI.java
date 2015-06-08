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
import com.yulius.belitungtourism.response.CarRentalResponseData;

public class CarRentalAPI extends BaseAPI{
    private OnResponseListener mOnResponseListener;

    public CarRentalAPI(Context context){
        super(context);

        TAG = "CarRentalAPI";
    }

    public void requestCarRentalList(){
        String url = API_CAR_RENTAL_URL;
        Log.v("TEST Request (CarRentalAPI)", "request car rental list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (CarRentalAPI)", response);
                }

                Gson gson = new Gson();
                CarRentalResponseData carResponseData = null;

                try {
                    carResponseData = gson.fromJson(response, CarRentalResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in car rental list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(carResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (CarRentalAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(CarRentalResponseData carResponseData);
    }
}
