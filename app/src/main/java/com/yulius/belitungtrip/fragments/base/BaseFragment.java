package com.yulius.belitungtrip.fragments.base;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.VolleyError;
import com.yulius.belitungtrip.Constans;
import com.yulius.belitungtrip.activities.MainActivity;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;

public class BaseFragment extends Fragment {

    public String TAG;
    protected String mTitle;
    protected String mAnalyticsScreenName;

    //================================================================================
    // Common Fragment Variable
    //================================================================================

    protected Context mContext;
    protected ContentResolver mResolver;
    protected LayoutInflater mLayoutInflater;
    protected View mLayoutView;

    //================================================================================
    // Constructor
    //================================================================================

    public BaseFragment() {
        super();
    }

    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = "BaseFragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getParentActivity();
        mResolver = mContext.getContentResolver();
        mLayoutInflater = LayoutInflater.from(mContext);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hideMessageScreen();
        setOnMessageActionListener(new OnMessageActionListener(){
            @Override
            public void onMessageActionTryAgain() {
                hideMessageScreen();
                refreshFragment();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        getParentActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //================================================================================
    // Option Menu
    //================================================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (!getParentActivity().mNavigationDrawerFragment.isDrawerOpen()) {
            inflateMenu(menu, inflater);
            restoreActionBar();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getParentActivity().getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setSubtitle(null);
//        actionBar.setIcon(R.drawable.icon_traveloka);

        restoreCustomActionBar(actionBar);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getParentActivity().onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //================================================================================
    // Utility Method For Maintaining Fragment Called By Inherit Class
    //================================================================================

    public void replaceContentFragment(Fragment newFragment){
        replaceContentFragment(newFragment, null);
    }

    public void replaceContentFragment(Fragment newFragment, String tag){
        ((BaseContainerFragment) getParentFragment()).replaceFragment(newFragment, tag);
    }

    public void initContentFragment(Fragment newFragment, String tag){

        if(getFragmentManager().getBackStackEntryCount() > 0) {
            int firstFragmentId = getFragmentManager().getBackStackEntryAt(0).getId();
            getFragmentManager().popBackStack(firstFragmentId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getParentActivity().setDrawerIndicatorEnabled(true);
        }

        ((BaseContainerFragment) getParentFragment()).initFragment(newFragment, tag);
    }

    /*
     * Must Get Activity from Parent Fragment
     *
     */
    public MainActivity getParentActivity(){
        return (MainActivity)(getParentFragment().getActivity());
    }

    protected void refreshFragment(){
        hideMessageScreen();
    }

    //================================================================================
    // Additional Method To Be Implement By Inherit Class
    //================================================================================

    protected void restoreCustomActionBar(ActionBar actionBar) {
    }

    public void inflateMenu(Menu menu, MenuInflater inflater){
    }

    //================================================================================
    // Utility Method For Show Error Message Called By Inherit Class
    //================================================================================

    public void showMessage(String message, Constans.MessageType messageType, int durationTime) {
        if(getParentFragment() instanceof BaseContainerFragment) {
            ((BaseContainerFragment) getParentFragment()).showMessage(message, messageType, durationTime);
        }
    }

    public void showMessageScreen(Constans.MessageScreenType messageScreenType, String fragmentTag){
        Fragment activeFragment = (Fragment) getFragmentManager().findFragmentByTag(fragmentTag);

        if(activeFragment != null){
            if(getParentFragment() instanceof BaseContainerFragment) {
                ((BaseContainerFragment) getParentFragment()).showMessageScreen(messageScreenType);
            }
        }
    }

    public void hideMessageScreen(){
        if(getParentFragment() instanceof BaseContainerFragment) {
            ((BaseContainerFragment) getParentFragment()).hideMessageScreen();
        }
    }

    public void showLoadingMessage(String fragmentTag){
        showMessageScreen(Constans.MessageScreenType.MESSAGE_SCREEN_LOADING, fragmentTag);
    }

    public void showRequestFailedErrorMessage(String message){
        showMessage(message, Constans.MessageType.MESSAGE_ERROR, Constans.Duration.MEDIUM);
    }

    public void showConnectionProblemErrorMessage(VolleyError volleyError, String fragmentTag){
        showMessageScreen(Constans.MessageScreenType.MESSAGE_SCREEN_NO_INTERNET_CONNECTION, fragmentTag);
    }

    public void setOnMessageActionListener(OnMessageActionListener onMessageActionListener){
        if(getParentFragment() instanceof BaseContainerFragment) {
            ((BaseContainerFragment) getParentFragment()).setOnMessageActionListener(onMessageActionListener);
        }
    }

    protected void hiddenKeyboard() {
        if(getView() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }


}
