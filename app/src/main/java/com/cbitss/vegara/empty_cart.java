package com.cbitss.vegara;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class empty_cart extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    EditText domain;
    Button search;
    private ArrayList<String> Name = new ArrayList<>();
    private ArrayList<String> Price = new ArrayList<>();
    private ArrayList<String> F1 = new ArrayList<>();
    private ArrayList<String> F2 = new ArrayList<>();
    private ArrayList<String> F3 = new ArrayList<>();
    private ArrayList<String> F4 = new ArrayList<>();
    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> Bank = new ArrayList<>();
    private ArrayList<String> logo = new ArrayList<>();
    ImageView icon_status;
    TextView status_text,status_message;
    LinearLayout error_holder;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_cart);
        domain = findViewById(R.id.domain_search);
        search = findViewById(R.id.search);
        icon_status = findViewById(R.id.status_icon);
        status_text = findViewById(R.id.status_text);
        status_message = findViewById(R.id.status_message);
        error_holder = findViewById(R.id.error_holder);
        progressBar = findViewById(R.id.progressBar);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        Log.d("navigation",navController.toString()+"   "+navigationView.toString());


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Domain = domain.getText().toString();

                if (TextUtils.isEmpty(Domain)){
                    domain.setError("Please you desired domain");
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    SharedPreferences shared = getSharedPreferences("user",MODE_PRIVATE);
                    String Phone = shared.getString("phone",null);
                    String id = shared.getString("id",null);

                    final geturl x = new geturl(Phone,id);
                    String url = x.searchurl(Domain);
                    Log.d("url_search",url);
                    RequestQueue requestQueue = Volley.newRequestQueue(empty_cart.this);
                    requestQueue.getCache().clear();
                    JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray status = response.getJSONArray("status");
                                JSONObject status1 = status.getJSONObject(0);
                                String Status = status1.getString("status");
                                if(Status.equals("available")) {
                                    error_holder.setVisibility(View.INVISIBLE);
                                    JSONArray pakage = response.getJSONArray("pakage");
                                    for (int i = 0; i < pakage.length(); i++) {
                                        JSONObject obj = pakage.getJSONObject(i);
                                        Log.d("response", obj.getString("name") + "id " + obj.getString("package_id") + "price " +
                                                obj.getString("price") + "features" + obj.getString("f1") + obj.getString("f2") +
                                                obj.getString("f3") + obj.getString("f4"));
                                        Name.add(obj.getString("name"));
                                        Price.add(obj.getString("price"));
                                        F1.add(obj.getString("f1"));
                                        F2.add(obj.getString("f2"));
                                        F3.add(obj.getString("f3"));
                                        F4.add(obj.getString("f4"));
                                        Id.add(obj.getString("package_id"));

                                    }
                                    JSONArray bank = response.getJSONArray("bank");
                                    for (int i = 0; i < bank.length(); i++) {
                                        JSONObject obj_bank = bank.getJSONObject(i);
                                        Log.d("response_bank", obj_bank.getString("name") + obj_bank.getString("logo"));
                                        Bank.add(obj_bank.getString("name"));
                                        logo.add(obj_bank.getString("logo"));
                                    }
                                    Intent i = new Intent(empty_cart.this, com.cbitss.vegara.pakage.class);
                                    Log.d("araylist", Name.get(0));


                                    i.putExtra("name", Name);
                                    i.putExtra("price", Price);
                                    i.putExtra("f1", F1);
                                    i.putExtra("f2", F2);
                                    i.putExtra("f3", F3);
                                    i.putExtra("f4", F4);
                                    i.putExtra("id", Id);
                                    i.putExtra("bank", Bank);
                                    i.putExtra("geturl", x);
                                    i.putExtra("logo",logo);
                                    startActivity(i);
                                    Name.clear();
                                    Price.clear();
                                    F4.clear();
                                    F3.clear();
                                    F2.clear();
                                    F1.clear();
                                    Id.clear();
                                    Bank.clear();
                                    logo.clear();
                                    progressBar.setVisibility(View.GONE);
                                }
                                else if (Status.equals("taken")){
                                    error_holder.setVisibility(View.VISIBLE);
                                    icon_status.setImageResource(R.drawable.taken);
                                    status_text.setText("Domain NOT AVAILABLE!");
                                    status_text.setTextColor(Color.parseColor("#ff0000"));
                                    status_message.setText("Please Search Again");
                                    progressBar.setVisibility(View.GONE);
                                }
                                else if (Status.equals("error")){
                                    error_holder.setVisibility(View.VISIBLE);
                                    String reason = status1.getString("reason");
                                    icon_status.setImageResource(R.drawable.error);
                                    status_text.setTextColor(Color.parseColor("#fdb71b"));
                                    status_text.setText("Search Failed");
                                    status_message.setText(reason+" Try Again or Contact Support");
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                Log.d("response","catch block ran");
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error",error.toString());
                            Toast.makeText(empty_cart.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                    requestQueue.add(jsonobjectRequest);
                }
            }
        });
    }
}