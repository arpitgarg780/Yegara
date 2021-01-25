package com.cbitss.vegara.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.cbitss.vegara.MainActivity;
import com.cbitss.vegara.R;
import com.cbitss.vegara.geturl;

public class verify extends AppCompatActivity {
    EditText otp;
    Button verify;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        otp = findViewById(R.id.otp_get);
        verify = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progressBar);
        final geturl x = (geturl)getIntent().getSerializableExtra("urlObj");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Otp = otp.getText().toString().trim();
                if(TextUtils.isEmpty(Otp)){
                    otp.setError("Please Enter the Otp");
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    final String url = x.verifyOtpUrl(Otp);
                    RequestQueue requestqueue = Volley.newRequestQueue(verify.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("ok")) {
                                Toast.makeText(verify.this, "Verified Succesfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(verify.this, MainActivity.class));
                                SharedPreferences shrd = getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor editor = shrd.edit();

                                editor.putString("id",x.getId());
                                editor.putString("phone",x.getPhone());
                                editor.putString("email",x.getEmail());
                                editor.putString("status","logedin");
                                editor.apply();
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(verify.this, response, Toast.LENGTH_SHORT).show();
                                Log.d("url verify",url);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(verify.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    requestqueue.add(stringRequest);
                }

            }
        });

    }
}