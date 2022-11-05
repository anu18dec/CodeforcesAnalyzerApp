package com.example.codeforcesanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChartView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.search_btn);
        editText = findViewById(R.id.edittext);
        problemsBar = findViewById(R.id.rating_count_chart);
        levelBar = findViewById(R.id.level_count_chart);
        languageView = findViewById(R.id.language_chart);
        verdictView = findViewById(R.id.verdict_chart);
        internalLayout = findViewById(R.id.internal_root);
        ratingTitleView = findViewById(R.id.rating_view_title);
        levelTitleView = findViewById(R.id.level_view_title);
        languageTitleView = findViewById(R.id.language_view_title);
        verdictTitleView = findViewById(R.id.verdict_view_title);
        ratingCardView = findViewById(R.id.card_view_rating_count);
        levelCardView = findViewById(R.id.card_view_level_count);
        languageCardView = findViewById(R.id.card_view_languages);
        verdictCardView = findViewById(R.id.card_view_verdict);
        indicator = findViewById(R.id.progress_indicator);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userName = Objects.requireNonNull(editText.getText()).toString();
                indicator.setVisibility(View.VISIBLE);
                run(userName);
            }
        });

    }


    void run(String userName)
    {
        String problemSetUrl = "https://codeforces.com/api/user.status?handle=" + userName;

        CallApi callApi = new CallApi();
        callApi.RequestData(this, problemSetUrl, new VolleyCallBack() {
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