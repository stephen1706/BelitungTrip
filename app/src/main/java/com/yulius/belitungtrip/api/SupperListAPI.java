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
import com.yulius.belitungtrip.response.SupperListResponseData;
public class SupperListAPI extends BaseAPI {

    private OnResponseListener mOnResponseListener;

    public SupperListAPI(Context context){
        super(context);

        TAG = "SupperListAPI";
    }

    public void requestSupperList(){
        String url = API_SUPPER_LIST_URL;
        Log.v("TEST Request (SupperListAPI)", "request supper list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (SupperListAPI)", response);
                }

                Gson gson = new Gson();
                SupperListResponseData supperListResponseData = null;

                try {
                    supperListResponseData = gson.fromJson(response, SupperListResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in supper list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(supperListResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (SupperListAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(SupperListResponseData supperListResponseData);
    }
}
