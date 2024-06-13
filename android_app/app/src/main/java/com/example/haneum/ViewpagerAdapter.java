package com.example.haneum;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStateAdapter {

    Fragment[] fragments = new Fragment[2];

    private ArrayList<Fragment> mFragments;

    public ViewpagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList list) {

        super(fragmentActivity);
        this.mFragments = list;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
