package com.yulius.belitungtrip.fragments.content;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.yulius.belitungtrip.R;

public class MapboxDialogFragment extends DialogFragment {
    private static final String PARAM_LATLNG = "param_latlng";
    private static final int DEFAULT_ZOOM_LEVEL = 14;

    private ImageView mCloseImageView;
    private View mLayoutView;
    private Dialog mDialog;

    private MapView mMapbox;
    private LatLng mLatLng;

    public static MapboxDialogFragment newInstance(LatLng latlng){
        MapboxDialogFragment fragment = new MapboxDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(PARAM_LATLNG, latlng);
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
            mLatLng = getArguments().getParcelable(PARAM_LATLNG);
        }

        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mLayoutView = inflater.inflate(R.layout.dialog_mapbox, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mLayoutView);

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);

        setUpAttribute();
        setUpView();
        setUpViewState();
        setUpListener();

        return mDialog;
    }

    private void setUpAttribute() {

    }

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
        mCloseImageView = (ImageView)mLayoutView.findViewById(R.id.image_view_close);
        mMapbox = (MapView) mLayoutView.findViewById(R.id.mapid);

        mMapbox.setCenter(mLatLng);
        Marker hotelMarker = new Marker(mMapbox,"", "", mLatLng);
        hotelMarker.setIcon(new Icon(getResources().getDrawable(R.drawable.green_pin)));
        mMapbox.addMarker(hotelMarker);
        mMapbox.setZoom(DEFAULT_ZOOM_LEVEL);
    }

    private void setUpViewState() {

    }

    private void setUpListener() {
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

}
