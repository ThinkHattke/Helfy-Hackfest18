package com.example.gaurav.helfy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gaurav.helfy.Adapter.OrdersAdapter;
import com.example.gaurav.helfy.Model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;

    private ScrollView scrollView;

    LinearLayoutManager linearLayoutManager;

    OrdersAdapter adapter;

    public ArrayList<String> reqId = new ArrayList<String>();
    public ArrayList<String> type = new ArrayList<String>();
    public ArrayList<String> from = new ArrayList<String>();
    public ArrayList<String> timeStamp = new ArrayList<String>();
    public ArrayList<String> _id = new ArrayList<String>();
    public ArrayList<String> location = new ArrayList<String>();
    public ArrayList<String> status = new ArrayList<String>();
    private ArrayList<Order> listContentArr= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        scrollView = findViewById(R.id.scrollView);

        request();

    }


    public void request() {
        RequestQueue queue = Volley.newRequestQueue(Home.this);
        final String url;
        url = "http://192.168.43.141:9966/api/request/Smile Baby";
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray = null;
                String orgId="";

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("requests");
                    orgId = jsonObject.getString("id");

                    for (int i = 0; i<jsonArray.length(); i++) {

                        JSONObject post = jsonArray.getJSONObject(i);

                        reqId.add(post.getString("reqId"));
                        type.add(post.getString("reqType"));
                        from.add(post.getString("from"));
                        timeStamp.add(post.getString("timeStamp"));
                        status.add("New");
                        location.add("Mumbai");
                        _id.add(post.getString("_id"));

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                recyclerView = findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                adapter = new OrdersAdapter(Home.this);

                for (int iter = 1; iter <=jsonArray.length(); iter++) {

                    Order pojoObject = new Order();

                    pojoObject.setType(type.get(iter-1));
                    pojoObject.setName(from.get(iter-1));
                    pojoObject.setAddress(location.get(iter-1));
                    pojoObject.setTimesatmp(timeStamp.get(iter-1));
                    pojoObject.setReqID(reqId.get(iter-1));
                    pojoObject.setStatus(status.get(iter-1));
                    pojoObject.set_id(_id.get(iter-1));

                    listContentArr.add(pojoObject);
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Home.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                adapter.setListContent(listContentArr);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);

                fade();

                final String finalOrgId = orgId;
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(Home.this, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);

                            Intent go = new Intent(Home.this, Detail.class);
                            go.putExtra("id", reqId.get(position));
                            go.putExtra("orgid", finalOrgId);
                            go.putExtra("name", listContentArr.get(position).getName());
                            go.putExtra("timestamp", listContentArr.get(position).getTimesatmp());
                            go.putExtra("address", listContentArr.get(position).getAddress());
                            go.putExtra("status", listContentArr.get(position).getStatus());
                            go.putExtra("type", listContentArr.get(position).getType());
                            startActivity(go);

                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    }
                });

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY",error.toString());
                    }
                }

        );

        queue.add(postRequest);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.social_media_item:
                Intent intent1 = new Intent(Home.this, SocialMedia.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.statistics_item:
                Intent i = new Intent(Home.this, Statistics.class);
                startActivity(i);
                finish();
                break;
            case R.id.funds_item:
                Intent intent = new Intent(Home.this, Funds.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


    public void print(String s) {
        Toast.makeText(Home.this, s, Toast.LENGTH_SHORT).show();
    }


    private void fade() {


        recyclerView.setAlpha(0f);


        scrollView.setVisibility(View.GONE);


        recyclerView.setVisibility(View.VISIBLE);


        recyclerView.animate().alpha(1f).setDuration(500);


    }

}
