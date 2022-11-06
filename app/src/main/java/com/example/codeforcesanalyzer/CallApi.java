package com.example.codeforcesanalyzer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NoConnectionError;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONObject;

public class CallApi
{
    private RequestQueue requestQueue;


    public void RequestData(Context context, String url, final VolleyCallBack callBack, LinearProgressIndicator indicator)
    {

        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            callBack.onSuccess(response);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        indicator.setVisibility(View.GONE);
                        if (error instanceof TimeoutError) {
                            Toast.makeText(context, "Time Out", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(context, "Your device is not connected to internet.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Something went wrong, maybe wrong username.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                );
        requestQueue.add(jsonObjectRequest);

    }
}
