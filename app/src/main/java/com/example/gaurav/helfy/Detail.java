package com.example.gaurav.helfy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detail extends AppCompatActivity {

    CircleImageView pic;

    TextView statusText, addresstext, typetext, itemtext, nametext;

    RelativeLayout itemBox;

    String name, status, timestamp, email, mobile, type, item, orgid, id, address;

    LinearLayout statusBox, detailsBox;

    ImageView arrow;

    Button accept, reject;

    Boolean arrowed = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Donation Info");
        setSupportActionBar(toolbar);toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backpress_white_icon);

        pic = findViewById(R.id.image);
        nametext = findViewById(R.id.name);
        statusText = findViewById(R.id.status);
        addresstext = findViewById(R.id.address);
        typetext = findViewById(R.id.type);
        itemtext = findViewById(R.id.item);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        statusBox = findViewById(R.id.statusBox);
        itemBox = findViewById(R.id.itemBox);
        arrow = findViewById(R.id.detailsArrow);
        detailsBox = findViewById(R.id.detailsBox);

        statusBox.setVisibility(View.GONE);

        Picasso.with(this)
                .load("https://pbs.twimg.com/profile_images/481447978545074177/uDGDtyNO.jpeg")
                .error(R.drawable.user_icon)
                .placeholder(R.drawable.user_icon)
                .into(pic);

        final Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String key;

        timestamp = bundle.getString("timestamp");
        nametext.setText(bundle.getString("name"));
        statusText.setText(bundle.getString("status"));
        addresstext.setText(bundle.getString("address"));
        typetext.setText(bundle.getString("type"));
        if (Objects.equals(bundle.get("type"), "Monetary")){
            itemtext.setText(bundle.getString("type"));
        } else {
            itemBox.setVisibility(View.GONE);
        }
        id = bundle.getString("id");
        orgid = bundle.getString("orgid");

        if (statusText.getText().equals("New")){
            statusBox.setVisibility(View.VISIBLE);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(id, orgid);
                statusText.setText("Accepted");
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject(id);
                statusText.setText("Rejected");
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (arrowed) {

                    arrow.setRotation(180);
                    arrowed = false;
                    detailsBox.setVisibility(View.GONE);

                } else {

                    arrow.setRotation(1f);
                    arrowed = true;
                    detailsBox.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Intent i = new Intent(Detail.this, Home.class);
        startActivity(i);
        finish();

    }


    private void updateStatus(String status) {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url;
        url = "http://therishabhsingh.com//helfy/updatestatus.php?id="+id+"&status="+status+"&orgid="+orgid+"&timestamp="+timestamp;
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                print("Status updated successfully");
                statusBox.setVisibility(View.GONE);

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","error"+error.toString());
                    }
                }

        );

        queue.add(postRequest);

    }


    private void accept(String reqId, String ngoId) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.43.141:9966/api/accept";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("reqid", reqId);
            jsonBody.put("ngoid", ngoId);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    print("Status updated successfully");
                    statusBox.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    print("Status updated successfully");
                    statusBox.setVisibility(View.GONE);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {

                        responseString = String.valueOf(response.statusCode);
                        print(responseString);
                        Log.e("VOLLEY", responseString);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void reject (String reqId) {


        RequestQueue queue = Volley.newRequestQueue(this);
        final String url;
        url = "http://192.168.43.141:9966/api/reject/"+reqId;
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.POST, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                print("Status Rejected successfully");
                statusBox.setVisibility(View.GONE);

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","error"+error.toString());
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


    public void print(String s) {
        Toast.makeText(Detail.this, s, Toast.LENGTH_SHORT).show();
    }

}
