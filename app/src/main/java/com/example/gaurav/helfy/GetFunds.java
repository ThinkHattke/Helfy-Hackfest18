package com.example.gaurav.helfy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class GetFunds extends AppCompatActivity {


    String[] reasons = {"Reason [Select]","Construction", "Accessories", "Daily Expense", "Emergency", "Electronics", "Bills", "Transportation", "Salary Payments", "Others"};

    String Reason="", others="", amount="";

    EditText Others, Amount;

    Button Submit;

    int balance;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_funds);

        final Bundle bundle = getIntent().getExtras();

        balance = Integer.parseInt(bundle.getString("balance"));

        Spinner spinner = findViewById(R.id.reason);
        Others = findViewById(R.id.other);
        Amount = findViewById(R.id.amount);
        Submit = findViewById(R.id.button);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reasons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Reason = reasons[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    addpayment(amount,"70");
                }
            }
        });

    }


    private void addpayment(String amount, String ngoId) {

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.43.141:9966/api/transact";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("amount", "-"+amount);
            jsonBody.put("ngoid", ngoId);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    print("Transaction proceeded successfully");
                    Intent i = new Intent(GetFunds.this, Funds.class);
                    startActivity(i);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    print("errored");
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


    public Boolean checkFields() {

        others = Others.getText().toString().trim();
        amount = Amount.getText().toString().trim();

        if (Reason.equals("Reason [Select]")){
            print("Choose a Reason for a withdrawal to continue");
        }

        if (Reason.equals("Others")){
            if (others.isEmpty()){
                print("Enter a reason for the withdrawal");
                return false;
            } else {
                Reason = others;
            }
        } else if (amount.isEmpty()){
            print("Enter a specific amount for the withdrawal");
            return false;
        } else if (Integer.parseInt(amount) > balance) {

            print("You don't have enough balance to withdraw the amount");
            return false;

        }


        return true;

    }


    public void print(String s) {
        Toast.makeText(GetFunds.this, s, Toast.LENGTH_SHORT).show();
    }


}
