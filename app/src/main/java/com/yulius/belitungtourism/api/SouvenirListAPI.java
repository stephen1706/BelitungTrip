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
import com.yulius.belitungtourism.response.SouvenirListResponseData;

public class SouvenirListAPI extends BaseAPI {
    private OnResponseListener mOnResponseListener;

    public SouvenirListAPI(Context context){
        super(context);

        TAG = "SouvenirListAPI";
    }

    public void requestSouvenirList(){
        String url = API_SOUVENIR_LIST_URL;
        Log.v("TEST Request (SouvenirListAPI)", "request souvenir list");

        BaseRequest stringRequest = new BaseRequest(Request.Method.POST, url, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(BuildConfig.DEBUG) {
                    Log.v("TEST Response (SouvenirListAPI)", response);
                }

                Gson gson = new Gson();
                SouvenirListResponseData souvenirListResponseData = null;

                try {
                    souvenirListResponseData = gson.fromJson(response, SouvenirListResponseData.class);
                } catch (Exception e){
                    Log.e(TAG, "error parsing json in souvenir list, " + e.getMessage());
                }

                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestSuccess(souvenirListResponseData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mOnResponseListener!=null){
                    mOnResponseListener.onRequestError(volleyError);
                    Log.v("TEST Response (SouvenirListAPI)", "error : " + volleyError.getMessage());
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
        public void onRequestSuccess(SouvenirListResponseData souvenirListResponseData);
    }
}
