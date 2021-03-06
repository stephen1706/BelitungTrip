package com.yulius.belitungtrip.api.base;

import android.content.ContentResolver;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.yulius.belitungtrip.api.RequestManager;

public class BaseAPI {

    //================================================================================
    // Base Constant
    //================================================================================

    public String TAG = "BaseAPI";


//    protected static final String BASE_API_URL ="192.168.1.41";
//    protected static final String BASE_API_URL = "yulius.comuv.com";
    protected static final String BASE_API_URL = "yulius.webatu.com";

    protected static final String BASE_API_HTTP_URL = "http://" + BASE_API_URL;


    //================================================================================
    // API Routes
    //================================================================================


    /**
     * Hotel Search
     */
    protected static final String API_HOTEL_LIST_ROUTES = "/yulius/HotelList.php";
    protected static final String API_HOTEL_DETAIL_ROUTES = "/yulius/HoteLDetail.php";

    protected static final String API_HOTEL_LIST_URL = BASE_API_HTTP_URL + API_HOTEL_LIST_ROUTES;
    protected static final String API_HOTEL_DETAIL_URL = BASE_API_HTTP_URL + API_HOTEL_DETAIL_ROUTES;



    //================================================================================
    // Base Class Variable, Constructor, and Listener
    //================================================================================

    protected Context mContext;
    protected RequestQueue mRequestQueue;
    protected ContentResolver mResolver;

    protected OnBaseResponseListener mOnBaseResponseListener;

    public BaseAPI(Context context) {
        mContext = context;
        mResolver = mContext.getContentResolver();

        mRequestQueue = RequestManager.getRequestQueue();
    }

    public interface OnBaseResponseListener {
        public void onRequestError(VolleyError volleyError);
        public void onRequestFailed(String message);
    }

    //================================================================================
    // Common function for API
    //================================================================================

    public void setOnResponseListener(OnBaseResponseListener onBaseResponseListener){
        mOnBaseResponseListener = onBaseResponseListener;
    }
}
