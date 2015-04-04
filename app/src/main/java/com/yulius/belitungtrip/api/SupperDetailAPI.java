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
import com.yulius.belitungtrip.response.SupperDetailResponseData;

import java.util.HashMap;
import java.util.Map;

public class SupperDetailAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public SupperDetailAPI(Context context){
        super(context);

        TAG = "SupperDetailAPI";
    }
    public void requestSupperDetail(final String supperId){
        String url = API_SUPPER_DETAIL_URL;
        Log.v("TEST Request (SupperDetailAPI)", "request supper detail with id : " + supperId);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (SupperDetailAPI)", response);
                }

                Gson gson = new Gson();
                SupperDetailResponseData supperDetailResponseData = null;

                try {
                    supperDetailResponseData = gson.fromJson(response, SupperDetailResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in supper detail, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(supperDetailResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (SupperDetailAPI)", "error : " + volleyError.getMessage());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",supperId);

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
        public void onRequestSuccess(SupperDetailResponseData supperDetailResponseData);
    }
}
