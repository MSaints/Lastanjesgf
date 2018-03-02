package com.afrofx.code.anjesgf.adpters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.afrofx.code.anjesgf.Fragments.ListaProdutosFragment;

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

}
