package com.yulius.belitungtourism.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.fragments.content.MainPagerFragment;

import java.util.ArrayList;

/**
 * Created by elroysetiabudi on 5/19/15.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener
{
    private ArrayList<String> homePagerImageList;
    private Activity activity;

    private ArrayList<MainPagerFragment> homePagerFragmentList;

    public MainPagerAdapter(FragmentManager fragmentManager, Activity activity, ArrayList<String> homePagerImageList)
    {
        super(fragmentManager);

        this.homePagerImageList = homePagerImageList;
        initiateFragment();
    }

    @Override
    public Fragment getItem(int position)
    {
        return homePagerFragmentList.get(position);
    }

    @Override
    public int getCount()
    {return homePagerFragmentList.size();}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {}

    @Override
    public void onPageSelected(int position)
    {}

    @Override
    public void onPageScrollStateChanged(int state)
    {}

    public void initiateFragment()
    {
        homePagerFragmentList = new ArrayList<MainPagerFragment>();
        for (int i=0; i< homePagerImageList.size(); ++i)
        {
            homePagerFragmentList.add(new MainPagerFragment());

            Bundle args = new Bundle();
            args.putString("resource_image", homePagerImageList.get(i));
            homePagerFragmentList.get(i).setArguments(args);
        }
    }
}
