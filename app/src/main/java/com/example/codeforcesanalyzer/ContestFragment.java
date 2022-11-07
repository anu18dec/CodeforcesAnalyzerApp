package com.example.codeforcesanalyzer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContestFragment extends Fragment {
    View view;
    LinearProgressIndicator indicatorContest;
    RecyclerView recyclerView;
    ArrayList<ContestModel> contestList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contest, container, false);
        return view;
    }

    void loadContests()
    {
        indicatorContest = view.findViewById(R.id.progress_indicator_contest);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        indicatorContest.setVisibility(View.VISIBLE);

        searchContests(indicatorContest);
        RecyclerAdapter adapter = new RecyclerAdapter(getContext(),contestList);
        recyclerView.setAdapter(adapter);
        indicatorContest.setVisibility(View.GONE);

    }


    void searchContests(LinearProgressIndicator indicator)
    {
        contestList.clear();

        String contestSetUrl = "https://codeforces.com/api/contest.list?gym=false";
        CallApi callApi = new CallApi();
        callApi.RequestData(getContext(), contestSetUrl, new VolleyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException
            {
                BarView barView = new BarView(jsonObject);
                barView.setContestlist(contestList);
            }
        },indicator);

    }
}