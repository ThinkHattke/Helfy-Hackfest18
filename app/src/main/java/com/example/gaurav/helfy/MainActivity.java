package com.example.gaurav.helfy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button login;

    LinearLayout register;

    String Email="", Password="";

    EditText email, password;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.loginButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    tryLogin();
                }

            }
        });

        register = findViewById(R.id.createAccount);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void tryLogin()  {

        progressDialog = ProgressDialog.show(this, "Loading", "Loading... Please wait", true, false);

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url;
        url = "http://therishabhsingh.com/helfy/login.php?email="+Email+"&password="+Password;
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                JSONArray jsonArray = null;

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("data");

                    JSONObject post = jsonArray.getJSONObject(0);

                    String whatToDo = post.getString("reponse");

                    if (whatToDo.equals("yes")){
                        Intent i = new Intent(MainActivity.this, Home.class);
                        startActivity(i);
                        finish();
                    } else {
                        print("Invalid Email and Password");
                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


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

    public Boolean checkFields() {

        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (Email.isEmpty()) {
            print("Enter email id to continue");
            return false;
        }  else if (Password.isEmpty()){
            print("Enter Password to continue");
            return false;
        }

        return true;

    }

    public void print(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }


}
