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
import com.yulius.belitungtrip.response.FlightResponseData;

public class FlightAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public FlightAPI(Context context){
        super(context);

        TAG = "FlightAPI";
    }

    public void requestFlightList(){
        String url = API_FLIGHT_URL;
        Log.v("TEST Request (FlightAPI)", "request flight list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (FlightAPI)", response);
                }

                Gson gson = new Gson();
                FlightResponseData flightResponseData = null;

                try {
                    flightResponseData = gson.fromJson(response, FlightResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in flight list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(flightResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (FlightAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(FlightResponseData flightResponseData);
    }
}
