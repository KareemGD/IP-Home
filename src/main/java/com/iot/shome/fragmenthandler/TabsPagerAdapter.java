package com.iot.shome.fragmenthandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iot.shome.InfoFrag;
import com.iot.shome.VoiceFrag;
import com.iot.shome.WebViewFrag;

/**
 * Created by Kareem Diab on 11/17/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new WebViewFrag();
            case 1:
                // Info fragment activity
                return new InfoFrag();
            case 2:
                // Voice fragment activity
                return new VoiceFrag();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
