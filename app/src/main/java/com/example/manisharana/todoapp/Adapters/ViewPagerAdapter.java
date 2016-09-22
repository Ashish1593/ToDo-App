package com.example.manisharana.todoapp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manisharana.todoapp.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankit on 21/9/16.
 */



public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;


    private final List<Fragment> mFragments = new ArrayList<Fragment>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}