package com.example.nsrin.tirupatibalaji;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int no_of_tabs;
    CommonListingFragment clf;

    public PagerAdapter(FragmentManager fm, int TabCount){

        super(fm);
        no_of_tabs=TabCount;
    }

    @Override
    public Fragment getItem(int position) {
        clf=new CommonListingFragment();
        clf.mParam1=position;
        return clf;
    }

    @Override
    public int getCount() {

        return no_of_tabs;
    }

}
