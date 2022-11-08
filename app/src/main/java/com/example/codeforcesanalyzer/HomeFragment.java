package com.example.codeforcesanalyzer;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.anychart.AnyChartView;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class HomeFragment extends Fragment {

    View view;
    private ExtendedFloatingActionButton searchButton;
    private TextInputEditText editText;
    private ConstraintLayout internalLayout;
    private MaterialTextView ratingTitleView;
    private MaterialTextView levelTitleView;
    private MaterialTextView languageTitleView;
    private MaterialTextView verdictTitleView;
    private CardView ratingCardView;
    private CardView levelCardView;
    private CardView languageCardView;
    private CardView verdictCardView;
    private LinearProgressIndicator indicator;
    BarChart problemsBar;
    BarChart levelBar;
    AnyChartView languageView;
    AnyChartView verdictView;
    MenuItem menuItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);


        searchButton = view.findViewById(R.id.search_btn);
        editText = view.findViewById(R.id.edittext);
        problemsBar = view.findViewById(R.id.rating_count_chart);
        levelBar = view.findViewById(R.id.level_count_chart);
        languageView = view.findViewById(R.id.language_chart);
        verdictView = view.findViewById(R.id.verdict_chart);
        internalLayout = view.findViewById(R.id.internal_root);
        ratingTitleView = view.findViewById(R.id.rating_view_title);
        levelTitleView = view.findViewById(R.id.level_view_title);
        languageTitleView = view.findViewById(R.id.language_view_title);
        verdictTitleView = view.findViewById(R.id.verdict_view_title);
        ratingCardView = view.findViewById(R.id.card_view_rating_count);
        levelCardView = view.findViewById(R.id.card_view_level_count);
        languageCardView = view.findViewById(R.id.card_view_languages);
        verdictCardView = view.findViewById(R.id.card_view_verdict);
        indicator = view.findViewById(R.id.progress_indicator);
        menuItem = view.findViewById(R.id.contest);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userName = Objects.requireNonNull(editText.getText()).toString();
                indicator.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                ContestFragment contestFragment = (ContestFragment)fragmentManager.findFragmentByTag("contest");
                assert contestFragment != null;
                contestFragment.loadContests();

                searchSubmissions(userName);


            }
        });

        return view;

    }

    void searchSubmissions(String userName)
    {
        String problemSetUrl = "https://codeforces.com/api/user.status?handle=" + userName;

        CallApi callApi = new CallApi();
        callApi.RequestData(getContext(), problemSetUrl, new VolleyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException
            {
                    BarView barView = new BarView(jsonObject);
                    barView.setProblemBar();
                    barView.setLevelBar();
                    barView.setVerdictChart();
                    barView.setLanguageChart();
                    indicator.setVisibility(View.GONE);
                    showViews();
                    barView.showProblemBarsChart(problemsBar);
                    barView.showLevelBarsChart(levelBar);
                    barView.showVerdictChart(verdictView);
                    barView.showLanguageChart(languageView);


            }
        },indicator);

    }





    void showViews()
    {
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(300);
        autoTransition.setInterpolator(new FastOutSlowInInterpolator());
        autoTransition.setStartDelay(0);
        TransitionManager.beginDelayedTransition(internalLayout,autoTransition);

        ratingCardView.setVisibility(View.VISIBLE);
        levelCardView.setVisibility(View.VISIBLE);
        verdictCardView.setVisibility(View.VISIBLE);
        languageCardView.setVisibility(View.VISIBLE);
        ratingTitleView.setVisibility(View.VISIBLE);
        levelTitleView.setVisibility(View.VISIBLE);
        languageTitleView.setVisibility(View.VISIBLE);
        verdictTitleView.setVisibility(View.VISIBLE);

    }
}