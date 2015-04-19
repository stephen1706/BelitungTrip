package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.BudgetSpinnerAdapter;
import com.yulius.belitungtourism.api.HotelMaxMinAPI;
import com.yulius.belitungtourism.api.PoiMaxMinAPI;
import com.yulius.belitungtourism.api.RestaurantMaxMinAPI;
import com.yulius.belitungtourism.response.BudgetMaxMinResponseData;

import java.util.ArrayList;

public class NewTripDialogFragment extends DialogFragment {
    private static final String PARAM_TOTAL_NIGHT = "param_total_night";
    private static final String PARAM_POI_MAX_BUDGET = "param_poi_budget";
    private static final String PARAM_RESTAURANT_MAX_BUDGET = "param_restaurant_budget";
    private static final String PARAM_HOTEL_MAX_BUDGET = "param_hotel_budget";
    private static final String PARAM_POI_MIN_BUDGET = "param_poi_min_budget";
    private static final String PARAM_RESTAURANT_MIN_BUDGET = "param_restaurant_min_budget";
    private static final String PARAM_HOTEL_MIN_BUDGET = "param_hotel_min_budget";
    private ImageView mCloseImageView;
    private View mLayoutView;
    private Dialog mDialog;
    private Button mContinueButton;
    private Context mContext;
    private EditText mTotalNightEditText;
    private EditText mHotelBudgetEditText;
    private EditText mRestaurantBudgetEditText;
    private EditText mPoiBudgetEditText;
    private Spinner mPoiBudgetSpinner;
    private Spinner mHotelBudgetSpinner;
    private Spinner mRestaurantBudgetSpinner;
    private BudgetSpinnerAdapter mPoiAdapter;
    private BudgetSpinnerAdapter mRestaurantAdapter;
    private BudgetSpinnerAdapter mHotelAdapter;
    private HotelMaxMinAPI mHotelMaxMinAPI;
    private RestaurantMaxMinAPI mRestaurantMaxMinAPI;
    private PoiMaxMinAPI mPoiMaxMinAPI;
    private BudgetMaxMinResponseData mPoiMaxMinResponseData;
    private BudgetMaxMinResponseData mRestaurantMaxMinResponseData;
    private BudgetMaxMinResponseData mHotelMaxMinResponseData;
    private ProgressDialog mProgressDialog;
    private String mDays;

    public static NewTripDialogFragment newInstance(){
        NewTripDialogFragment fragment = new NewTripDialogFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;

    }

    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayoutView = inflater.inflate(R.layout.dialog_new_trip, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(mLayoutView);

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);

        setUpView();
        setUpViewState();
        setUpRequestAPI();
        setUpListener();

        return mDialog;
    }

    /*
     * There is bug with compatibility library, but don't know what is the cause
     * Here is the link:
     * http://stackoverflow.com/questions/14657490/how-to-properly-retain-a-dialogfragment-through-rotation
     */
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    //================================================================================
    // Set Up
    //================================================================================

    private void setUpView() {
        mCloseImageView = (ImageView) mLayoutView.findViewById(R.id.image_view_close);
        mContinueButton = (Button) mLayoutView.findViewById(R.id.button_continue);
//        mRestaurantBudgetEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_restaurant_budget);
//        mPoiBudgetEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_poi_budget);
//        mHotelBudgetEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_hotel_budget);
        mTotalNightEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_total_night);
        mPoiBudgetSpinner = (Spinner) mLayoutView.findViewById(R.id.spinner_poi_budget);
        mHotelBudgetSpinner = (Spinner) mLayoutView.findViewById(R.id.spinner_hotel_budget);
        mRestaurantBudgetSpinner = (Spinner) mLayoutView.findViewById(R.id.spinner_restaurant_budget);
    }

    private void setUpViewState() {
        setCancelable(false);
    }

    private void setUpRequestAPI() {
        mHotelMaxMinAPI = new HotelMaxMinAPI(mContext);
        mHotelMaxMinAPI.setOnResponseListener(new HotelMaxMinAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(BudgetMaxMinResponseData budgetMaxMinResponseData) {
                mHotelMaxMinResponseData = budgetMaxMinResponseData;
                mProgressDialog.dismiss();
                setUpAdapter();
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "No Internet Connection, please re-input the number of days", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String message) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "No Internet Connection, please re-input the number of days", Toast.LENGTH_SHORT).show();
            }
        });

        mRestaurantMaxMinAPI = new RestaurantMaxMinAPI(mContext);
        mRestaurantMaxMinAPI.setOnResponseListener(new RestaurantMaxMinAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(BudgetMaxMinResponseData budgetMaxMinResponseData) {
                mRestaurantMaxMinResponseData = budgetMaxMinResponseData;
                mHotelMaxMinAPI.requestHotelMaxMin(mDays);
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "No Internet Connection, please re-input the number of days", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String message) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "No Internet Connection, please re-input the number of days", Toast.LENGTH_SHORT).show();
            }
        });

        mPoiMaxMinAPI = new PoiMaxMinAPI(mContext);
        mPoiMaxMinAPI.setOnResponseListener(new PoiMaxMinAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(BudgetMaxMinResponseData budgetMaxMinResponseData) {
                mPoiMaxMinResponseData = budgetMaxMinResponseData;
                mRestaurantMaxMinAPI.requestRestaurantMaxMin(mDays);
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "No Internet Connection, please re-input the number of days", Toast.LENGTH_SHORT).show();            }

            @Override
            public void onRequestFailed(String message) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, "No Internet Connection, please re-input the number of days", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpAdapter(){
        ArrayList<Integer> poiList = new ArrayList<Integer>();
        int minimumPrice = mPoiMaxMinResponseData.lowest;
        int maximumPrice = mPoiMaxMinResponseData.highest;
        int range = (maximumPrice - minimumPrice)/5;
        for(int i=0;i<5;i++){
            poiList.add(range*i + minimumPrice);
        }
        mPoiAdapter = new BudgetSpinnerAdapter(mContext, range);
        mPoiAdapter.addAll(poiList);

        ArrayList<Integer> restaurantList = new ArrayList<Integer>();
        minimumPrice = mRestaurantMaxMinResponseData.lowest;
        maximumPrice = mRestaurantMaxMinResponseData.highest;
        range = (maximumPrice - minimumPrice)/5;
        for(int i=0;i<5;i++){
            restaurantList.add(range*i + minimumPrice);
        }
        mRestaurantAdapter = new BudgetSpinnerAdapter(mContext, range);
        mRestaurantAdapter.addAll(restaurantList);

        ArrayList<Integer> hotelList = new ArrayList<Integer>();
        minimumPrice = mHotelMaxMinResponseData.lowest;
        maximumPrice = mHotelMaxMinResponseData.highest;
        range = (maximumPrice - minimumPrice)/5;
        for(int i=0;i<5;i++){
            hotelList.add(range*i + minimumPrice);
        }
        mHotelAdapter = new BudgetSpinnerAdapter(mContext, range);
        mHotelAdapter.addAll(hotelList);

        mPoiBudgetSpinner.setAdapter(mPoiAdapter);
        mRestaurantBudgetSpinner.setAdapter(mRestaurantAdapter);
        mHotelBudgetSpinner.setAdapter(mHotelAdapter);
    }

    private void setUpListener() {
        mTotalNightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){
                    if(Integer.parseInt(s.toString()) <= 5 && Integer.parseInt(s.toString()) >= 2){
                        mDays = s.toString();
                        mPoiMaxMinAPI.requestPoiMaxMin(mDays);
                        mProgressDialog = ProgressDialog.show(mContext, "Please Wait", "Getting price range..", true);

                    } else {
                        Toast.makeText(mContext, "Minimum trip duration is 2 days and maximum trip duration is 5 days", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelDialog();
            }
        });
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                long totalNight;
                try {
                    totalNight = Long.parseLong(mTotalNightEditText.getText().toString());
                } catch (Exception e){
                    Toast.makeText(mContext, "Please fill all the required field correctly", Toast.LENGTH_LONG).show();
                    return;
                }
                if(totalNight <= 1) {
                    Toast.makeText(mContext, "Minimal trip duration is 2 days", Toast.LENGTH_LONG).show();
                    return;
                }

                if(totalNight > 5){
                    Toast.makeText(mContext, "Maximum trip duration is 5 days", Toast.LENGTH_LONG).show();
                    return;
                }

                long minPoiBudget = (int) mPoiBudgetSpinner.getSelectedItem();
                long poiBudget = minPoiBudget + mPoiAdapter.getRange();

                long minRestaurantBudget = (int) mRestaurantBudgetSpinner.getSelectedItem();
                long restaurantBudget = minRestaurantBudget + mRestaurantAdapter.getRange();

                long minHotelBudget = (int) mHotelBudgetSpinner.getSelectedItem();
                long hotelBudget = minRestaurantBudget + mHotelAdapter.getRange();

//                long minBudget = (totalNight-1)*500000/3;
//                if(poiBudget < minBudget || hotelBudget < minBudget || restaurantBudget < minBudget){
//                    Toast.makeText(mContext, "Minimum budget for every part is Rp " + minBudget, Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                long maxBudget = 50000000;
//                if(poiBudget > maxBudget || hotelBudget > maxBudget || restaurantBudget > maxBudget){
//                    Toast.makeText(mContext, "Maximum budget for every part is Rp " + maxBudget, Toast.LENGTH_LONG).show();
//                    return;
//                }
                i.putExtra(PARAM_TOTAL_NIGHT, totalNight);
                i.putExtra(PARAM_RESTAURANT_MAX_BUDGET, restaurantBudget);
                i.putExtra(PARAM_RESTAURANT_MIN_BUDGET, minRestaurantBudget);
                i.putExtra(PARAM_HOTEL_MAX_BUDGET, hotelBudget);
                i.putExtra(PARAM_HOTEL_MIN_BUDGET, minHotelBudget);
                i.putExtra(PARAM_POI_MAX_BUDGET, poiBudget);
                i.putExtra(PARAM_POI_MIN_BUDGET, minPoiBudget);
                NewTripDialogFragment.this.dismiss();

                if(getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                }
            }
        });
    }

    private void onCancelDialog() {
        mDialog.dismiss();
    }
}
