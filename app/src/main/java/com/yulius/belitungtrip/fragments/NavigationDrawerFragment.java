package com.yulius.belitungtrip.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yulius.belitungtrip.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {

    public static final int HOME_SECTION = 0;
    public static final int HOTEL_SECTION = 1;
    public static final int RESTAURANT_SECTION = 2;
    public static final int POI_SECTION = 3;
    public static final int TRIP_PLANNER_SECTION = 4;
    public static final int TRANSPORTATION_SECTION = 5;

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mDrawerView;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private SectionAdapter mSectionAdapter;
    private SectionItem userSectionItem;

    public NavigationDrawerFragment() {
    }

    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mDrawerView = (View)inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView = (ListView)mDrawerView.findViewById(R.id.drawer_list_view);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        setUpSectionAdapter();
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);


        return mDrawerView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //================================================================================
    // Action Bar
    //================================================================================

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        hiddenKeyboard();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle(null);

        mSectionAdapter.notifyDataSetChanged();
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close   /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;

                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }



    //================================================================================
    // Set Up
    //================================================================================

    private void setUpSectionAdapter(){
        ArrayList<SectionItem> sectionItems = new ArrayList<SectionItem>();
        sectionItems.add(new SectionItem(R.drawable.icon_home, "Home"));
        sectionItems.add(new SectionItem(R.drawable.icon_hotel, "Hotel"));
        sectionItems.add(new SectionItem(R.drawable.icon_restaurant, "Restaurant"));
        sectionItems.add(new SectionItem(R.drawable.icon_poi, "Objek Wisata"));
        sectionItems.add(new SectionItem(R.drawable.icon_trip_planner, "Trip Planner"));
        sectionItems.add(new SectionItem(R.drawable.icon_car, "Transportasi"));

        mSectionAdapter = new SectionAdapter(getActivity(), -1, sectionItems);
        mDrawerListView.setAdapter(mSectionAdapter);
    }


    //================================================================================
    // Adapter
    //================================================================================

    private class SectionAdapter extends ArrayAdapter<SectionItem> {
        private Context mContext;
        private LayoutInflater mInflater;
        List<SectionItem> mSectionItems;

        private SectionViewHolder mSectionViewHolder;

        public SectionAdapter(Context context, int layoutResourceId, List<SectionItem> sectionItems) {
            super(context, layoutResourceId, sectionItems);

            mContext = context;
//            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mInflater = LayoutInflater.from(context);

            mSectionItems = sectionItems;
        }

        @Override
        public int getCount() {
            return mSectionItems.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_drawer_section, parent, false);

                mSectionViewHolder = new SectionViewHolder();

                mSectionViewHolder.sectionFrame = (LinearLayout)convertView.findViewById(R.id.section_frame);
                mSectionViewHolder.sectionIconImageView = (ImageView)convertView.findViewById(R.id.section_icon_image_view);
                mSectionViewHolder.sectionTitleTextView = (TextView)convertView.findViewById(R.id.section_title_text_view);
                convertView.setTag(mSectionViewHolder);
            }
            else{
                mSectionViewHolder = (SectionViewHolder)convertView.getTag();
            }

            SectionItem currentSection = mSectionItems.get(position);

            mSectionViewHolder.sectionIconImageView.setImageResource(currentSection.iconResource);
            mSectionViewHolder.sectionTitleTextView.setText(currentSection.title);


            if(mCurrentSelectedPosition == position){
                mSectionViewHolder.sectionFrame.setAlpha(1.0f);
                mSectionViewHolder.sectionFrame.setBackgroundColor(getResources().getColor(R.color.active_section_background));
            }
            else{
                mSectionViewHolder.sectionFrame.setAlpha(0.6f);
                mSectionViewHolder.sectionFrame.setBackgroundColor(getResources().getColor(R.color.inactive_section_background));
            }

            return convertView;
        }
    }

    private class SectionItem{
        public int iconResource;
        public String title;

        public SectionItem(int iconResource, String title){
            this.iconResource = iconResource;
            this.title = title;
        }
    }

    private class SectionViewHolder {
        LinearLayout sectionFrame;
        ImageView sectionIconImageView;
        TextView sectionTitleTextView;
        TextView notificationIconTextView;
    }



    //================================================================================
    // Called From Activity
    //================================================================================

    public void setDrawerIndicatorEnabled(boolean enabled){
        mDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }


    //================================================================================
    // Interface Callback
    //================================================================================

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    protected void hiddenKeyboard() {
        if(getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

}
