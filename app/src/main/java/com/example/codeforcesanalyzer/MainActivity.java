package com.example.codeforcesanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.example.codeforcesanalyzer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    final Fragment homeFragment = new HomeFragment();
    final Fragment contestFragment = new ContestFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = homeFragment;
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        fragmentManager.beginTransaction().add(R.id.frame_layout,contestFragment, "contest").hide(contestFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout,homeFragment, "home").commit();


        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId())
            {
                case R.id.home:
                    fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;
                case R.id.contest:
                    fragmentManager.beginTransaction().hide(active).show(contestFragment).commit();
                    active = contestFragment;
                    return true;
            }
            return false;
        });

    }
}