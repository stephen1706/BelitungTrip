package com.yulius.belitungtourism.fragments.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yulius.belitungtourism.R;

public class MainPagerFragment extends Fragment
{
    private View view;
    private ImageView imageView;

    public MainPagerFragment()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.pager_main, container, false);

        String resourceString = getArguments().getString("resource_image");
        imageView = (ImageView) view.findViewById(R.id.imageViewMainPager);
        imageView.setImageResource(getActivity().getResources().getIdentifier(resourceString, "drawable", getActivity().getPackageName()));

        return view;
    }
}