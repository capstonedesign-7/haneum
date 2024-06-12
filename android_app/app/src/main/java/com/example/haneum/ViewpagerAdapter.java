package com.example.haneum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStateAdapter {

    Fragment[] fragments = new Fragment[2];

    private ArrayList<Fragment> mFragments;

    public ViewpagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList list) {

        /*
        super(fragmentActivity);
        fragments[0] = new FirstFragment();
        fragments[1] = new SecondFragment();

         */
        super(fragmentActivity);
        this.mFragments = list;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position); //position 은 현재 만들어야 할 index번호
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
