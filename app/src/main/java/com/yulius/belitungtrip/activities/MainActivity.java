package com.yulius.belitungtrip.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.fragments.NavigationDrawerFragment;
import com.yulius.belitungtrip.fragments.base.BaseContainerFragment;
import com.yulius.belitungtrip.fragments.containers.HotelContainerFragment;
import com.yulius.belitungtrip.fragments.containers.MainContainerFragment;
import com.yulius.belitungtrip.fragments.containers.PoiContainerFragment;
import com.yulius.belitungtrip.fragments.containers.RestaurantContainerFragment;
import com.yulius.belitungtrip.fragments.containers.TransportationContainerFragment;
import com.yulius.belitungtrip.fragments.containers.TripPlannerContainerFragment;


public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
//
    private StreetViewPanorama svp;

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng INDONESIA = new LatLng(-6.193157,106.820194);

    public static final String HOME_SECTION_TAG = "Home Search";
    public static final String HOTEL_SECTION_TAG = "Hotel";
    public static final String RESTAURANT_SECTION_TAG = "Restaurant";
    public static final String POI_SECTION_TAG = "Poi";
    public static final String TRIP_PLANNER_SECTION_TAG = "Trip Planner";
    public static final String TRANSPORTATION_SECTION_TAG = "Transportation";

    //================================================================================
    // Current Activity Variable
    //================================================================================

    public NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private FragmentTabHost mFragmentTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpTabHost();
        setUpNavigationDrawer();

        //setUpStreetViewPanoramaIfNeeded(savedInstanceState);
    }

    private void startPanorama() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.panorama.PanoramaViewActivity"));
        intent.setDataAndType(Uri.parse("file://" + "/sdcard/DCIM/Camera/20140829_131503.jpg"), "image/*");
        startActivity(intent);
    }

    private void setUpStreetViewPanoramaIfNeeded(Bundle savedInstanceState) {
        if (svp == null) {
            svp = ((SupportStreetViewPanoramaFragment)
                    getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama))
                    .getStreetViewPanorama();
            if (svp != null) {
                if (savedInstanceState == null) {
                    svp.setPosition("QKZJDF5QWO0AAAAAAAABOw");
                    StreetViewPanoramaLocation location = svp.getLocation();
                    if (location != null && location.links != null) {
                        Log.d("Test pano","panoid : " + location.links[0].panoId);
                        svp.setPosition(location.links[0].panoId);
                    } else{
                        Log.d("Test pano","ga ada");
                    }
                }
            }
        }

    }

    private void setUpTabHost() {
        mFragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.fragment_section_container);

        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(HOME_SECTION_TAG).setIndicator(HOME_SECTION_TAG), MainContainerFragment.class, null);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(HOTEL_SECTION_TAG).setIndicator(HOTEL_SECTION_TAG), HotelContainerFragment.class, null);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(RESTAURANT_SECTION_TAG).setIndicator(RESTAURANT_SECTION_TAG), RestaurantContainerFragment.class, null);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(POI_SECTION_TAG).setIndicator(POI_SECTION_TAG), PoiContainerFragment.class, null);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(TRANSPORTATION_SECTION_TAG).setIndicator(TRANSPORTATION_SECTION_TAG), TransportationContainerFragment.class, null);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec(TRIP_PLANNER_SECTION_TAG).setIndicator(TRIP_PLANNER_SECTION_TAG), TripPlannerContainerFragment.class, null);

        mFragmentTabHost.getTabWidget().setVisibility(View.GONE);

        switchTab(NavigationDrawerFragment.HOME_SECTION);
    }
    private void setUpNavigationDrawer() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switchTab(position);
    }

    public String getCurrentFragmentTag(){
        return mFragmentTabHost.getCurrentTabTag();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        BaseContainerFragment currentContainerFragment = (BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(this.mFragmentTabHost.getCurrentTabTag());
        FragmentManager childFragmentManager = currentContainerFragment.getChildFragmentManager();
        Fragment currentFragment = childFragmentManager.findFragmentById(currentContainerFragment.getContainerId());
        currentFragment.onActivityResult(requestCode, resultCode, data);
    }
    public void switchTab(int position) {
        switch (position){
            case NavigationDrawerFragment.HOME_SECTION:
                mTitle = "Belitung Trip"; //edit title actionbar
                mFragmentTabHost.setCurrentTabByTag(HOME_SECTION_TAG);
                break;
            case NavigationDrawerFragment.HOTEL_SECTION:
                mTitle = "Daftar Hotel";
                mFragmentTabHost.setCurrentTabByTag(HOTEL_SECTION_TAG);
                break;
            case NavigationDrawerFragment.RESTAURANT_SECTION:
                mTitle = "Daftar Restoran";
                mFragmentTabHost.setCurrentTabByTag(RESTAURANT_SECTION_TAG);
                break;
            case NavigationDrawerFragment.POI_SECTION:
                mTitle = "Dafter Objek Wisata";
                mFragmentTabHost.setCurrentTabByTag(POI_SECTION_TAG);
                break;
            case NavigationDrawerFragment.TRIP_PLANNER_SECTION:
                mTitle = "Trip planner";
                mFragmentTabHost.setCurrentTabByTag(TRIP_PLANNER_SECTION_TAG);
                break;
            case NavigationDrawerFragment.TRANSPORTATION_SECTION:
                mTitle = "Daftar Transportasi";
                mFragmentTabHost.setCurrentTabByTag(TRANSPORTATION_SECTION_TAG);
                break;
        }
    }

    public void selectItem(int position){
        mNavigationDrawerFragment.selectItem(position);
    }

    public void setDrawerIndicatorEnabled(boolean enabled){
        mNavigationDrawerFragment.setDrawerIndicatorEnabled(enabled);
    }
    @Override
    public void onBackPressed() {
        Fragment currentContainerFragment = getSupportFragmentManager().findFragmentByTag(this.mFragmentTabHost.getCurrentTabTag());//ambil container yg skrg
        if(currentContainerFragment.getChildFragmentManager().getBackStackEntryCount() > 0){//klo ada fragment seblmny yg disimpen di stack
            if(currentContainerFragment.getChildFragmentManager().getBackStackEntryCount() == 1){
                setDrawerIndicatorEnabled(true);
            }
            Fragment currentFragment = currentContainerFragment.getChildFragmentManager().findFragmentById(R.id.container);
            currentContainerFragment.getChildFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
