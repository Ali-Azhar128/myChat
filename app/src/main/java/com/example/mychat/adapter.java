package com.example.mychat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mychat.fragments.SignupTabFragment;
import com.example.mychat.fragments.loginTabFragment;

public class adapter extends FragmentPagerAdapter {
    private String[] tabTitles = new String[]{"Login", "Sign Up"};
    private Context context;
    int totalTabs;

    public adapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount()
    {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                loginTabFragment logintabfragment = new loginTabFragment();
                return logintabfragment;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return null;
        }

    }




}
