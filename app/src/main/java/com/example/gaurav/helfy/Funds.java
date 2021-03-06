package com.example.gaurav.helfy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Funds extends AppCompatActivity {

    private static String TAG = "FUNDS";

    private float[] yData = {25.3f, 10.6f, 66.76f, 44.32f};
    private String[] xData = {"Monetary", "Cloths" , "Electronics" , "Accessories"};
    PieChart pieChart;
    private TextView funds;
    private LinearLayout textPoints;
    private ShimmerFrameLayout shimmerFrameLayout;

    LinearLayout Payout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds);

        pieChart = findViewById(R.id.piechart);
        funds = findViewById(R.id.points);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        textPoints = findViewById(R.id.amountLayout);

        Payout = findViewById(R.id.linearLayout);

        Payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Funds.this, GetFunds.class);
                i.putExtra("balance", funds.getText());
                startActivity(i);
            }
        });

        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(35f);
        pieChart.getDescription().setText("Donation Based upon its type");
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Donation Type");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(20);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.getLegend().setTextColor(Color.BLACK);

        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {



            }

            @Override
            public void onNothingSelected() {

            }
        });

        updateStatus();

    }


    private void updateStatus() {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url;
        url = "http://192.168.43.141:9966/api/balance/70";
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject object  = new JSONObject(response);

                    String point = object.getString("balance");

                    funds.setText(point);

                    shimmerFrameLayout.setVisibility(View.GONE);

                    textPoints.setAlpha(0f);

                    textPoints.setVisibility(View.VISIBLE);

                    textPoints.animate().alpha(1f).setDuration(500);


                } catch (JSONException e1) {

                    e1.printStackTrace();

                    shimmerFrameLayout.setVisibility(View.GONE);

                    textPoints.setAlpha(0f);

                    textPoints.setVisibility(View.VISIBLE);

                    textPoints.animate().alpha(1f).setDuration(500);

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("error","error"+error.toString());

                        textPoints.setAlpha(0f);

                        textPoints.setVisibility(View.VISIBLE);

                        textPoints.animate().alpha(1f).setDuration(500);

                    }
                }

        );

        queue.add(postRequest);

    }


    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i], xData[i]));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Donation Types");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.pieBlue));
        colors.add(getResources().getColor(R.color.pieGreen));
        colors.add(getResources().getColor(R.color.pieYellow));
        colors.add(getResources().getColor(R.color.pieOrange));

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Funds.this, Home.class);
        startActivity(i);
        finish();
    }
}
