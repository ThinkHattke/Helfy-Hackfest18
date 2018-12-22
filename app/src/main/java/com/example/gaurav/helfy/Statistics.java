package com.example.gaurav.helfy;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Statistics extends AppCompatActivity {

    TextView points;
    String point;
    LinearLayout textPoints;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        setSupportActionBar(toolbar);toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backpress_white_icon);

        points = findViewById(R.id.points);
        textPoints = findViewById(R.id.pointsLayour);
        shimmerFrameLayout = findViewById(R.id.shimmer);

        updateStatus();

    }


    private void updateStatus() {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url;
        url = "http://192.168.43.141:9966/api/rating/Smile Baby";
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject object  = new JSONObject(response);

                    point = object.getString("rating");

                    points.setText(point);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
