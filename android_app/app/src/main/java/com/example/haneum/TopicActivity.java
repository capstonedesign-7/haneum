package com.example.haneum;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    ViewPager2 pager2;
    ViewpagerAdapter adapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        String getSituation = getIntent().getStringExtra("situation");
        System.out.println(getSituation);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Fragment> fragments = new ArrayList<>();

        if (getSituation.equals("hospital")) {
            fragments.add(FirstFragment.newInstance(getSituation, "treatment"));
            fragments.add(SecondFragment.newInstance(getSituation, "treatment"));
        }


        /* View pager */
        pager2 = findViewById(R.id.viewPager);
        adapter = new ViewpagerAdapter(this, fragments);
        pager2.setAdapter(adapter);


        tabLayout = findViewById(R.id.tabLayout);

        //String[] tabTitle = {"TAB1","TAB2"};
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, pager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) { // 탭 숫자만큼 호출
                //탭 이름 설정 하는 곳
                //tab.setText(tabTitle[position]);

            }
        });

        mediator.attach();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
