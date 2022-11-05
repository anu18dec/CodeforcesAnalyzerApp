package com.example.codeforcesanalyzer;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallBack {
    void onSuccess(JSONObject jsonObject) throws JSONException;
}
