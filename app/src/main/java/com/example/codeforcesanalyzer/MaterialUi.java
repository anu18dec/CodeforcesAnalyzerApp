package com.example.codeforcesanalyzer;

import android.app.Application;
import com.google.android.material.color.DynamicColors;

public class MaterialUi extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
