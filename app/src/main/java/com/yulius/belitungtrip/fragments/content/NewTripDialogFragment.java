package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yulius.belitungtrip.R;

public class NewTripDialogFragment extends DialogFragment {
    private static final String PARAM_TOTAL_NIGHT = "param_total_night";
    private static final String PARAM_POI_BUDGET = "param_poi_budget";
    private static final String PARAM_RESTAURANT_BUDGET = "param_restaurant_budget";
    private static final String PARAM_HOTEL_BUDGET = "param_hotel_budget";
    private ImageView mCloseImageView;
    private View mLayoutView;
    private Dialog mDialog;
    private Button mContinueButton;
    private Context mContext;
    private EditText mTotalNightEditText;
    private EditText mHotelBudgetEditText;
    private EditText mRestaurantBudgetEditText;
    private EditText mPoiBudgetEditText;

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
        mRestaurantBudgetEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_restaurant_budget);
        mPoiBudgetEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_poi_budget);
        mHotelBudgetEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_hotel_budget);
        mTotalNightEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_total_night);

    }

    private void setUpViewState() {
        setCancelable(false);

    }

    private void setUpListener() {
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
                int poiBudget;
                int restaurantBudget;
                int hotelBudget;
                int totalNight;
                try {
                    poiBudget = Integer.parseInt(mPoiBudgetEditText.getText().toString());
                    restaurantBudget = Integer.parseInt(mRestaurantBudgetEditText.getText().toString());
                    hotelBudget = Integer.parseInt(mHotelBudgetEditText.getText().toString());
                    totalNight = Integer.parseInt(mTotalNightEditText.getText().toString());
                } catch (Exception e){
                    Toast.makeText(mContext, "Harap lengkapi field dengan benar", Toast.LENGTH_LONG).show();
                    return;
                }

                if(totalNight <= 0 || poiBudget <= 0 || hotelBudget <= 0 || restaurantBudget <= 0){
                    Toast.makeText(mContext, "Harap lengkapi field dengan benar", Toast.LENGTH_LONG).show();
                    return;
                }
                i.putExtra(PARAM_TOTAL_NIGHT, totalNight);
                i.putExtra(PARAM_RESTAURANT_BUDGET, restaurantBudget);
                i.putExtra(PARAM_HOTEL_BUDGET, hotelBudget);
                i.putExtra(PARAM_POI_BUDGET, poiBudget);
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
