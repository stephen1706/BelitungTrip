package com.yulius.belitungtourism.fragments.content;

import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

public class StreetViewFragment extends SupportStreetViewPanoramaFragment implements StreetViewPanorama.OnStreetViewPanoramaChangeListener{
    public static StreetViewFragment newInstance(double latitude, double longitude){
        Bundle args = new Bundle();
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);

        StreetViewFragment fragment =  new StreetViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View fragmentView = super.onCreateView(inflater, container, savedInstance);

        StreetViewPanorama panorama = getStreetViewPanorama();
        //Check we got a valid instance of the StreetViewPanorama
        if(panorama != null){
            double latitude = getArguments().getDouble("latitude", -1);
            double longitude = getArguments().getDouble("longitude", -1);
            panorama.setPosition(new LatLng(latitude, longitude));
            panorama.setOnStreetViewPanoramaChangeListener(this);
        }
        return fragmentView;
    }

    @Override
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation){
        //Get the angle between the target location and road side location
        float bearing = getBearing(
                streetViewPanoramaLocation.position.latitude,
                streetViewPanoramaLocation.position.longitude,
                getArguments().getDouble("latitude", -1),
                getArguments().getDouble("longitude", -1));

        //Remove the listener
        getStreetViewPanorama().setOnStreetViewPanoramaChangeListener(null);
        //Change the camera angle
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                .bearing(bearing)
                .build();
        getStreetViewPanorama().animateTo(camera, 1);
    }

    private float getBearing(double startLat, double startLng, double endLat, double endLng){
        Location startLocation = new Location("startlocation");
        startLocation.setLatitude(startLat);
        startLocation.setLongitude(startLng);

        Location endLocation = new Location("endlocation");
        endLocation.setLatitude(endLat);
        endLocation.setLongitude(endLng);

        return startLocation.bearingTo(endLocation);
    }

    private void startPanorama() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.panorama.PanoramaViewActivity"));
        intent.setDataAndType(Uri.parse("file://" + "/sdcard/DCIM/Camera/20140829_131503.jpg"), "image/*");
        startActivity(intent);
    }
}
