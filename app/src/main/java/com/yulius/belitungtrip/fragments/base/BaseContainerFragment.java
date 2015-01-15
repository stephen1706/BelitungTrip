package com.yulius.belitungtrip.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulius.belitungtrip.Constans;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;

public abstract class BaseContainerFragment extends Fragment{

    public String TAG;

    private static final String STATE_ERROR_MESSAGE = "state_error_message";

    //================================================================================
    // Common Container Fragment Variable
    //================================================================================

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    //================================================================================
    // Attribute used for Error Message
    //================================================================================

    public ViewGroup mMessageFrame;
    public TextView mMessageTextView;
    public ImageView mCloseImageView;

    public ViewGroup mMessageScreenFrame;

    private String mMessage;
    protected OnMessageActionListener mOnMessageActionListener;

    //================================================================================
    // Handler for delayed action
    //================================================================================

    private Handler mHandler;

    //================================================================================
    // Constructor
    //================================================================================

    public BaseContainerFragment() {
        super();

        TAG = "BaseContainerFragment";

        /**
         * Retain fragment instance.
         * If not retained, this will go back to the first fragment inserted
         */
        setRetainInstance(true);
    }


    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_container, container, false);
        mMessageFrame = (ViewGroup) view.findViewById(R.id.frame_message);
        mMessageTextView = (TextView) view.findViewById(R.id.text_view_message);
        mCloseImageView = (ImageView) view.findViewById(R.id.image_view_close);

        mMessageScreenFrame = (ViewGroup) view.findViewById(R.id.frame_message_screen);

        setUpListener();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_ERROR_MESSAGE, mMessage);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            String errorMessage = savedInstanceState.getString(STATE_ERROR_MESSAGE);
            setErrorMessage(errorMessage);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    //================================================================================
    // Set Up
    //================================================================================

    private void setUpListener() {
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithAnimation();
            }
        });
    }


    //================================================================================
    // Utility Method Called By Parent Activity
    //================================================================================

    public int getContainerId() {
        return R.id.container;
    }


    //================================================================================
    // Utility Method For Maintaining Fragment Called By Child Fragment
    //================================================================================

    public void initFragment(Fragment initialFragment) {
        initFragment(initialFragment, null);
    }

    public void initFragment(Fragment initialFragment, String tag) {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, initialFragment, tag);
        fragmentTransaction.commit();

    }
    public void replaceFragment(Fragment newFragment) {
        replaceFragment(newFragment, null);
    }

    public void replaceFragment(Fragment newFragment, String tag) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit, R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        fragmentTransaction.replace(R.id.container, newFragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public Fragment getCurrentContentFragment() {
        return getChildFragmentManager().findFragmentById(R.id.container);
    }


    //================================================================================
    // Utility Method For Show Error Message Called By Child Fragment
    //================================================================================

    public void showMessage(String message, Constans.MessageType messageType, int durationTime) {
        if(message == null) return;

        switch (messageType){
            case MESSAGE_SUCCESS:
                mMessageFrame.setBackgroundColor(getResources().getColor(R.color.message_success));
                break;
            case MESSAGE_WARNING:
                mMessageFrame.setBackgroundColor(getResources().getColor(R.color.message_warning));
                break;
            case MESSAGE_ERROR:
                mMessageFrame.setBackgroundColor(getResources().getColor(R.color.message_error));
                break;
        }

        if(!message.equals(mMessage) || mHandler == null){
            if(mHandler != null){
                mHandler.removeCallbacks(mHideRunnable);
                mHandler.removeCallbacks(mHideWithAnimationRunnable);
                mHandler = null;
            }
            setErrorMessage(message);
            mMessageFrame.setVisibility(View.VISIBLE);

            if(durationTime != Constans.Duration.INFINITE) {
                mHandler = new Handler();
                mHandler.postDelayed(mHideRunnable, durationTime);
            }

            final Animation animation = getShowAnimation();
            mMessageFrame.startAnimation(animation);
        }
    }

    public void showMessageScreen(Constans.MessageScreenType messageScreenType) {
        showMessageScreen(messageScreenType, "");
    }

    public void showMessageScreen(Constans.MessageScreenType messageScreenType, String message) {
        View messageScreenView = null;
        Button tryAgainButton = null;

        switch (messageScreenType){
            case MESSAGE_SCREEN_LOADING:
                messageScreenView = mLayoutInflater.inflate(R.layout.message_loading, mMessageScreenFrame, false);
//                ImageView loadingAnimationImageView = (ImageView)messageScreenView.findViewById(R.id.image_view_loading_animation);
//                AnimationDrawable loadingAnimation = (AnimationDrawable)loadingAnimationImageView.getBackground();
//                loadingAnimation.start();
                break;
            case MESSAGE_SCREEN_NO_INTERNET_CONNECTION:
                messageScreenView = mLayoutInflater.inflate(R.layout.message_no_internet_connection, mMessageScreenFrame, false);
                tryAgainButton = (Button)messageScreenView.findViewById(R.id.button_try_again);
                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMessageActionListener != null) {
                            mOnMessageActionListener.onMessageActionTryAgain();
                        }
                    }
                });
                break;
        }

        mMessageScreenFrame.removeAllViews();
        mMessageScreenFrame.addView(messageScreenView);
        mMessageScreenFrame.setVisibility(View.VISIBLE);
    }

    public void hideMessageScreen(){
        mMessageScreenFrame.setVisibility(View.GONE);
    }

    public void setOnMessageActionListener(OnMessageActionListener onMessageActionListener){
        mOnMessageActionListener = onMessageActionListener;
    }

    //================================================================================
    // Utility Method For Show Error Message (Private)
    //================================================================================

    private void setErrorMessage(String errorMessage) {
        mMessage = errorMessage;

        mMessageTextView.setText(mMessage);
    }

    private Animation getShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -0.75f, Animation.RELATIVE_TO_SELF, 0.0f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);

        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(250);

        return animationSet;
    }

    private Animation getDismissAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -0.75f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setDuration(250);

        return animationSet;
    }

    /**
     * This function must be called from UI Thread
     */
    private void dismissWithAnimation() {
        Animation animation = this.getDismissAnimation();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.post(mHideWithAnimationRunnable);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mMessageFrame.startAnimation(animation);
    }

    /**
     * This function must be called from UI Thread
     */
    private void dismissWithLayoutAnimation() {
        mMessageFrame.setVisibility(View.INVISIBLE);

        if(mHandler != null) {
            mHandler.removeCallbacks(mHideRunnable);
            mHandler.removeCallbacks(mHideWithAnimationRunnable);
            mHandler = null;
        }
    }

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            dismissWithAnimation();
        }
    };

    private final Runnable mHideWithAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            dismissWithLayoutAnimation();
        }
    };
}
