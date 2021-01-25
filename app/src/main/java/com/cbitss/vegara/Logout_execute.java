package com.cbitss.vegara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Logout_execute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_execute);

        final SharedPreferences shared = getSharedPreferences("user",MODE_PRIVATE);
        String status = shared.getString("status","notlogedin");
        String Phone = shared.getString("phone",null);
        String id = shared.getString("id",null);

        geturl x = new geturl(Phone,id);
        String url  = x.logouturl();
        Log.d("logout url",url);

        RequestQueue requestQueue = Volley.newRequestQueue(Logout_execute.this);
        requestQueue.getCache().clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")) {
                    Toast.makeText(Logout_execute.this, "Loged out Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Logout_execute.this, Mobile_enter.class));
                    SharedPreferences.Editor editor = shared.edit();

                    editor.putString("id", "");
                    editor.putString("phone", "");
                    editor.putString("status", "notlogedin");
                    editor.apply();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Logout_execute.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Logout_execute.this,MainActivity.class));
                finish();
            }
        });
        requestQueue.add(stringRequest);
    }
}