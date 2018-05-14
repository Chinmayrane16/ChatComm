package com.example.andoid.chatcomm;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Chinmay on 12-Apr-18.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";

                default:
                    return null;
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                RequestsFragment requestFragment = new RequestsFragment();
                return requestFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
