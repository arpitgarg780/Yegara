package com.cbitss.vegara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cbitss.vegara.ui.verify;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Mobile_enter extends AppCompatActivity {
    EditText phone, email;
    Button start;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_enter);
        phone = findViewById(R.id.mobileNumber);
        email = findViewById(R.id.email);
        start = findViewById(R.id.start);
        progressBar = findViewById(R.id.progressBar);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;


                String Phone = phone.getText().toString().trim();
                String Email = email.getText().toString().trim();
                if (TextUtils.isEmpty(Phone)) {
                    phone.setError("Phone Number is required");
                    flag = 1;
                } else if (Phone.length() != 8) {
                    phone.setError("Phone number should be 8 digit long");
                    flag = 1;
                }
                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email id is required");
                } else if (flag == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    final RequestQueue requestQueue = Volley.newRequestQueue(Mobile_enter.this);
                    randgenerate rand = new randgenerate(Phone);
                    String id = rand.randgenerateid();
                    final geturl x = new geturl(Phone,Email,id);
                    final String url = x.verifyRequestUrl();



                    final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (true){//response.equals("ok")) {
                                Toast.makeText(Mobile_enter.this, "Verification code sent", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Mobile_enter.this, verify.class);
                                i.putExtra("urlObj",x);
                                startActivity(i);
                                Log.d("url",url);
                                Log.d("api",response);
                                progressBar.setVisibility(View.GONE);
                            }
                            else {
                                Toast.makeText(Mobile_enter.this, response, Toast.LENGTH_SHORT).show();
                                Log.d("url",url);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Mobile_enter.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                    SmsRetrieverClient client = SmsRetriever.getClient(Mobile_enter.this);
                    final Task<Void> task = client.startSmsRetriever();
                    task.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Successfully started retriever, expect broadcast intent
                            // ...
                            requestQueue.add(stringRequest);
                            Log.d("api","Started retriever api");
                        }
                    });

                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to start retriever, inspect Exception for more details
                            // ...
                            Log.d("api","failed to start retriever api");
                        }
                    });

                }
            }
        });
    }

}