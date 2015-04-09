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
import com.yulius.belitungtourism.response.PoiListResponseData;

public class PoiListAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public PoiListAPI(Context context){
        super(context);

        TAG = "PoiListAPI";
    }

    public void requestPoiList(){
        String url = API_POI_LIST_URL;
        Log.v("TEST Request (PoiListAPI)", "request poi list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (PoiListAPI)", response);
                }

                Gson gson = new Gson();
                PoiListResponseData poiListResponseData = null;

                try {
                    poiListResponseData = gson.fromJson(response, PoiListResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in poi list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(poiListResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (PoiListAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(PoiListResponseData poiListResponseData);
    }
}
