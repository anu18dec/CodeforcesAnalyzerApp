package com.example.codeforcesanalyzer;


import android.graphics.Color;
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BarView
{
    private JSONObject obj;
    private JSONArray problem_array;
    private int len;
    private int []ratingArray;
    private int []levelArray;
    private Map<String,Integer> languageMap;
    private Map<String,Integer> verdictMap;

    //Constructor
    BarView(JSONObject obj) throws JSONException
    {
        this.obj  = obj;
        this.problem_array = obj.getJSONArray("result");
        len = problem_array.length();
    }

    //Methods to extract data from JsonObject
    public void setProblemBar() throws JSONException
    {
        ratingArray = new int[5001];
        Arrays.fill(ratingArray,0);

        JSONObject objProb , objProbInd;
        for (int i = 0; i < len; i++)
        {
            objProb = problem_array.optJSONObject(i);
            if(objProb.getString("verdict").equals("OK"))
            {

                objProbInd = objProb.optJSONObject("problem");

                //getting rating of each solved problem
                assert objProbInd != null;
                if (objProbInd.optInt("rating") > 0)
                {
                    ratingArray[objProbInd.optInt("rating")]++;
                }
            }
        }
    }

    public void setLevelBar() throws JSONException
    {
        levelArray = new int[27];
        Arrays.fill(levelArray,0);

        JSONObject objProb , objProbInd;
        for (int i = 0; i < len; i++)
        {
            objProb = problem_array.optJSONObject(i);
            if(objProb.getString("verdict").equals("OK"))
            {

                objProbInd = objProb.optJSONObject("problem");

                //getting A,B,C.. index of each solved problem
                assert objProbInd != null;
                String indx = objProbInd.optString("index");
                if(indx.charAt(0)>=65 && indx.charAt(0)<=90)
                {
                    levelArray[indx.charAt(0)-65]++;
                }
            }
        }

    }
    
    public void setVerdictChart()
    {
        verdictMap = new TreeMap<>();

        JSONObject objProb ;
        for (int i = 0; i < len; i++)
        {
            objProb = problem_array.optJSONObject(i);
            if(verdictMap.get(objProb.optString("verdict"))!=null)
            {
                Integer cnt = verdictMap.get(objProb.optString("verdict"));
                verdictMap.put(objProb.optString("verdict"),cnt+1);
            }
            else
            {
                verdictMap.put(objProb.optString("verdict"),1);
            }
        }

        if(verdictMap.get("OK")!=null)
        {
            Integer cnt = verdictMap.get("OK");
            verdictMap.remove("OK");
            verdictMap.put("AC",cnt);
        }
        if(verdictMap.get("WRONG_ANSWER")!=null)
        {
            Integer cnt = verdictMap.get("WRONG_ANSWER");
            verdictMap.remove("WRONG_ANSWER");
            verdictMap.put("WA",cnt);
        }
        if(verdictMap.get("TIME_LIMIT_EXCEEDED")!=null)
        {
            Integer cnt = verdictMap.get("TIME_LIMIT_EXCEEDED");
            verdictMap.remove("TIME_LIMIT_EXCEEDED");
            verdictMap.put("TLE",cnt);
        }
        if(verdictMap.get("MEMORY_LIMIT_EXCEEDED")!=null)
        {
            Integer cnt = verdictMap.get("MEMORY_LIMIT_EXCEEDED");
            verdictMap.remove("MEMORY_LIMIT_EXCEEDED");
            verdictMap.put("MLE",cnt);
        }
        if(verdictMap.get("COMPILATION_ERROR")!=null)
        {
            Integer cnt = verdictMap.get("COMPILATION_ERROR");
            verdictMap.remove("COMPILATION_ERROR");
            verdictMap.put("CPE",cnt);
        }
        if(verdictMap.get("RUNTIME_ERROR")!=null)
        {
            Integer cnt = verdictMap.get("RUNTIME_ERROR");
            verdictMap.remove("RUNTIME_ERROR");
            verdictMap.put("RE",cnt);
        }


    }

    public void setLanguageChart() throws JSONException
    {
        languageMap = new TreeMap<>();

        JSONObject objProb;
        for (int i = 0; i < len; i++)
        {
            objProb = problem_array.optJSONObject(i);

            //getting data for pie chart of languages
            if(languageMap.get(objProb.optString("programmingLanguage"))!=null)
            {
                Integer cnt = languageMap.get(objProb.optString("programmingLanguage"));
                languageMap.put(objProb.optString("programmingLanguage"),cnt+1);
            }
            else
            {
                languageMap.put(objProb.optString("programmingLanguage"),1);
            }
        }

    }


    //Methods to show charts in view
    public void showProblemBarsChart(BarChart barChart)
    {
        //Fixing zoom issue while reloding
        barChart.setFitBars(true);
        barChart.fitScreen();

        String title = "Rating counts";
        ArrayList<String> ratingLabels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        int baseRating;
        for (int i = 0, j = 0; i < ratingArray.length; i++)
        {
            if(ratingArray[i]>0)
            {
                baseRating = i;
                colors.add(getColor(i));
                entries.add(new BarEntry(j, ratingArray[i]));
                ratingLabels.add(Integer.toString(baseRating));
                j++;
            }

        }

        BarDataSet barDataSet = new BarDataSet(entries,title);
        barDataSet.setColors(colors);
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barChart.setData(barData);

        barChart.getXAxis().setLabelCount(entries.size());
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(ratingLabels));
        barChart.getXAxis().setGranularityEnabled(true);
        
        ratingBarChart_config(barChart);
        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }

    public void showLevelBarsChart(BarChart barChart)
    {
        //Fixing zoom issue while reloding
        barChart.setFitBars(true);
        barChart.fitScreen();

        String title = "Indexing counts";
        ArrayList<String> indexLabels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        int[] colors = new int[]
                {       Color.parseColor("#758283"),
                        Color.parseColor("#38CC77"),
                        Color.parseColor("#46B2E0"),
                        Color.parseColor("#FF6263"),
                        Color.parseColor("#38CC77"),
                        Color.parseColor("#E07C24"),
                };

        int baseIndexing;
        for (int i = 0, j = 0; i < levelArray.length; i++)
        {
            if(levelArray[i]>0)
            {
                baseIndexing = i;
                entries.add(new BarEntry(j,levelArray[i]));
                char ch = (char)(baseIndexing + 'A');
                indexLabels.add(ch+"");
                j++;
            }

        }

        BarDataSet barDataSet = new BarDataSet(entries,title);
        barDataSet.setColors(colors);
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barChart.setData(barData);

        barChart.getXAxis().setLabelCount(entries.size());
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(indexLabels));
        barChart.getXAxis().setGranularityEnabled(true);

        levelBarChart_config(barChart);
        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }

    public void showVerdictChart(AnyChartView verdictChart)
    {
        APIlib.getInstance().setActiveAnyChartView(verdictChart);
        Pie pie1 = AnyChart.pie();

        ArrayList<DataEntry> dataEntries1 = new ArrayList<DataEntry>();
        Set<Map.Entry<String, Integer> > map1 = verdictMap.entrySet();

        for (Map.Entry<String, Integer> mp1 : map1)
        {
            dataEntries1.add(new ValueDataEntry(mp1.getKey(),mp1.getValue()));
        }
        pie1.data(dataEntries1);
        pie1.sort("desc");
        pie1.selected().explode("3%");
        pie1.outline(true);
        pie1.animation(true,50);

        pie1.legend()
                .position("bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.TOP);
        pie1.legend().margin(20);

        verdictChart.setChart(pie1);


    }

    public void showLanguageChart(AnyChartView languageChart)
    {
        APIlib.getInstance().setActiveAnyChartView(languageChart);
        Pie pie = AnyChart.pie();

        ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
        Set<Map.Entry<String, Integer> > map = languageMap.entrySet();

        for (Map.Entry<String, Integer> mp : map)
        {
            dataEntries.add(new ValueDataEntry(mp.getKey(),mp.getValue()));
        }
        pie.data(dataEntries);
        pie.sort("desc");
        pie.selected().explode("3%");
        pie.outline(true);
        pie.animation(true,50);

        pie.legend()
                .position("bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.TOP);
        pie.legend().margin(20);

        languageChart.setChart(pie);

    }


    //Methods to configure charts
    public void ratingBarChart_config(BarChart barChart)
    {
        barChart.setTouchEnabled(true);
        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.setDrawBorders(false);
        barChart.setDrawGridBackground(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawLabels(true);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.animateXY(1000, 1000);
        barChart.setVisibleXRangeMaximum(8);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

    }

    public void levelBarChart_config(BarChart barChart)
    {
        barChart.setTouchEnabled(true);
        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.setDrawBorders(false);
        barChart.setDrawGridBackground(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.animateXY(1000, 1000);
        barChart.setVisibleXRangeMaximum(8);



        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

    }

    int getColor(int range)
    {
        if(range<1200 && range>=800)
            return Color.parseColor("#d1d1d1");
        else if(range<1400 && range>=1200)
            return Color.parseColor("#C5FF8C");
        else if(range<1600 && range>=1400)
            return Color.parseColor("#84e0c2");
        else if(range<1900 && range>=1600)
            return Color.parseColor("#aaaaff");
        else if(range<2100 && range>=1900)
            return Color.parseColor("#ff88ff");
        else if(range<2300 && range>=2100)
            return Color.parseColor("#ffcc88");
        else if(range<2400 && range>=2300)
            return Color.parseColor("#ffbb55");
        else if(range<2600 && range>=2400)
            return Color.parseColor("#ff7777");
        else if(range<3000 && range>=2600)
            return Color.parseColor("#ff3333");
        else
            return Color.parseColor("#aa0000");

    }



}
