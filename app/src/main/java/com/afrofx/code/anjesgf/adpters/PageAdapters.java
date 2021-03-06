package com.afrofx.code.anjesgf.adpters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afro FX on 2/6/2018.
 */

public class PageAdapters extends FragmentStatePagerAdapter {

    private final List<Fragment> istFragement = new ArrayList<>();
    private final List<String> tabTitles = new ArrayList<>();


    public PageAdapters(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return istFragement.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    public CharSequence getPageTitle(int position) {
        return (CharSequence) tabTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String title) {
        istFragement.add(fragment);
        tabTitles.add(title);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
