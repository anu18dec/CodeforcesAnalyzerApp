package com.example.codeforcesanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChartView;
import com.example.codeforcesanalyzer.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

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
                    break;
                case R.id.contest:
                    fragmentManager.beginTransaction().hide(active).show(contestFragment).commit();
                    active = contestFragment;
                    break;
            }
            return true;
        });

    }
}