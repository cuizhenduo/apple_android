package com.example.czd.kotlinone.adapter;

import android.support.v4.app.*;
import android.view.ViewGroup;

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public TabFragmentAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position % fragments.length];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       super.destroyItem(container, position, object);
    }
}
