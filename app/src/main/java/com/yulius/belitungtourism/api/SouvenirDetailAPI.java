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
import com.yulius.belitungtourism.response.SouvenirDetailResponseData;

import java.util.HashMap;
import java.util.Map;

public class SouvenirDetailAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public SouvenirDetailAPI(Context context){
        super(context);

        TAG = "SouvenirDetailAPI";
    }
    public void requestSouvenirDetail(final String souvenirId){
        String url = API_SOUVENIR_DETAIL_URL;
        Log.v("TEST Request (SouvenirDetailAPI)", "request souvenir detail with id : " + souvenirId);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (SouvenirDetailAPI)", response);
                }

                Gson gson = new Gson();
                SouvenirDetailResponseData souvenirDetailResponseData = null;

                try {
                    souvenirDetailResponseData = gson.fromJson(response, SouvenirDetailResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in souvenir detail, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(souvenirDetailResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (SouvenirDetailAPI)", "error : " + volleyError.getMessage());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",souvenirId);

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

    public interface OnResponseListener extends BaseAPI.OnBaseResponseListener {
        public void onRequestSuccess(SouvenirDetailResponseData souvenirDetailResponseData);
    }
}
