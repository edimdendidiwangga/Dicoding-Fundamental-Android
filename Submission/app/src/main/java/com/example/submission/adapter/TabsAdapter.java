package com.example.submission.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.submission.model.User;
import com.example.submission.R;
import com.example.submission.ui.FollowersFragment;
import com.example.submission.ui.FollowingsFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private User user;

    public TabsAdapter(Context mContext, FragmentManager fm, User user) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
        this.user = user;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.followers_user,
            R.string.following_user
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FollowersFragment();
                Bundle bundleFollowers = new Bundle();
                bundleFollowers.putString(FollowersFragment.EXTRA_FOLLOWERS, user.getLogin());
                fragment.setArguments(bundleFollowers);
                break;

            case 1:
                fragment = new FollowingsFragment();
                Bundle bundleFollowing = new Bundle();
                bundleFollowing.putString(FollowingsFragment.EXTRA_FOLLOWING, user.getLogin());
                fragment.setArguments(bundleFollowing);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
