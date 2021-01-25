package com.cbitss.vegara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cart_orders extends AppCompatActivity {
    ArrayList<String> Id = new ArrayList<>();
    ArrayList<String> Domain_name = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<String> Bank_name = new ArrayList<>();
    ArrayList<String> Account_name = new ArrayList<>();
    ArrayList<String> Account_no = new ArrayList<>();
    ArrayList<String> Pakage_name = new ArrayList<>();
    public RecyclerView recyclerView;

    @Override
    protected void onStart() {
        super.onStart();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_orders);
        SharedPreferences shared = getSharedPreferences("user",MODE_PRIVATE);
        String Phone = shared.getString("phone",null);
        String id = shared.getString("id",null);

        final geturl x = new geturl(Phone,id);
        final String url = x.carturl();
        RequestQueue requestQueue = Volley.newRequestQueue(cart_orders.this);
        Log.d("url_cart",url);
        Id.clear();
        Domain_name.clear();;
        Price.clear();
        Bank_name.clear();
        Account_name.clear();
        Account_no.clear();
        Log.d("debug_arrraylist",Id.toString()+Domain_name.toString()+Price.toString());
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("url_cart_check",url);
                    Log.d("cart response",response.toString());
                    JSONArray status = response.getJSONArray("status");
                    JSONObject status1 = status.getJSONObject(0);
                    Log.d("status",status1.getString("status"));
                    if(status1.getString("status").equals("empty")){
                        startActivity(new Intent(cart_orders.this, empty_cart.class));
//                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction trans = manager.beginTransaction();
//                        trans.remove(GalleryFragment);
//                        trans.commit();
//                        manager.popBackStack();
//                        manager.popBackStack();
                        finish();
                    }
                    else{
                        JSONArray obj = response.getJSONArray("order");
                        for (int i=0;i<obj.length()-1;i++){
                            JSONObject order = obj.getJSONObject(i);
                            Log.d("whole order json",order.toString());
                            Id.add(order.getString("order_id"));
                            Domain_name.add(order.getString("domain_name"));
//                            Pakage_name.add(order.getString("package_type"));
                            Price.add(order.getString("package_price"));
                            Bank_name.add(order.getString("bank_name"));
                            Account_name.add(order.getString("bank_account_name"));
                            Account_no.add(order.getString("bank_account_no"));
                            Log.d("temp_url",x.deleteorderurl(order.getString("domain_name"),order.getString("order_id")));
                            Log.d("debug_arrraylist_update",Id.toString()+Domain_name.toString()+Price.toString());

                        }
                        Log.d("data is",Id.toString());
                        recyclerView = findViewById(R.id.orders_list);
                        orders_recycler_adapter adapter = new orders_recycler_adapter(cart_orders.this,Id,Domain_name,Pakage_name,Price,Bank_name,Account_name,Account_no,x);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(cart_orders.this));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObject);
    }
}