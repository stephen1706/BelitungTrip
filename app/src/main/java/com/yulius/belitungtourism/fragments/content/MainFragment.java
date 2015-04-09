package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.fragments.base.BaseFragment;


public class MainFragment extends BaseFragment{
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.main_fragment_tag);
        mTitle = "Main Page";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_main, container, false);
//        Button b = (Button) mLayoutView.findViewById(R.id.button);
//        final ImageView mProductImage = (ImageView) mLayoutView.findViewById(R.id.image);
//        b.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
//                setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
//                setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
//
//                // Create new fragment to add (Fragment B)
//                Fragment fragment = TripPlannerHomeFragment.newInstance();
//                fragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
//                fragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
//
//                // Add Fragment B
//                FragmentTransaction ft = getFragmentManager().beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .addSharedElement(mProductImage, mProductImage.getTransitionName());
//                ft.commit();
//            }
//        });
//        setUpView();
//        setUpViewState();
//        setUpListener();
//        setUpRequestAPI();
//        setUpMessageListener();

        return mLayoutView;
    }
}
