package com.yulius.belitungtrip.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yulius.belitungtrip.BuildConfig;
import com.yulius.belitungtrip.api.base.BaseAPI;
import com.yulius.belitungtrip.api.base.BaseRequest;
import com.yulius.belitungtrip.response.CarResponseData;

public class CarAPI extends BaseAPI{
    private OnResponseListener mOnResponseListener;

    public CarAPI(Context context){
        super(context);

        TAG = "CarAPI";
    }

    public void requestCarList(){
        String url = API_CAR_URL;
        Log.v("TEST Request (CarAPI)", "request car list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (CarAPI)", response);
                }

                Gson gson = new Gson();
                CarResponseData carResponseData = null;

                try {
                    carResponseData = gson.fromJson(response, CarResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in car list, " + e.getMessage());
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
                    Log.v("TEST Response (CarAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(CarResponseData carResponseData);
    }
}
