package com.cbitss.vegara.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cbitss.vegara.MainActivity;
import com.cbitss.vegara.R;
import com.cbitss.vegara.geturl;
import com.cbitss.vegara.pakage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private ArrayList<String> Name = new ArrayList<>();
    private ArrayList<String> Price = new ArrayList<>();
    private ArrayList<String> F1 = new ArrayList<>();
    private ArrayList<String> F2 = new ArrayList<>();
    private ArrayList<String> F3 = new ArrayList<>();
    private ArrayList<String> F4 = new ArrayList<>();
    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> Bank = new ArrayList<>();
    private ArrayList<String> logo = new ArrayList<>();

    private HomeViewModel homeViewModel;
    Button search;
    EditText doamin;
    ImageView icon_status;
    TextView status_text,status_message;
    LinearLayout error_holder;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        search = root.findViewById(R.id.search);
        doamin = root.findViewById(R.id.domain_search);
        icon_status = root.findViewById(R.id.status_icon);
        status_text = root.findViewById(R.id.status_text);
        status_message = root.findViewById(R.id.status_message);
        error_holder = root.findViewById(R.id.error_holder);
        progressBar = root.findViewById(R.id.progress);
                search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String Domain = doamin.getText().toString();

                if (TextUtils.isEmpty(Domain)){
                    doamin.setError("Please you desired domain");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    SharedPreferences shared = getActivity().getSharedPreferences("user",MODE_PRIVATE);
                    String Phone = shared.getString("phone",null);
                    String id = shared.getString("id",null);

                    final geturl x = new geturl(Phone,id);
                    String url = x.searchurl(Domain);
                    Log.d("url_search",url);
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                        Intent i = new Intent(getActivity(), com.cbitss.vegara.pakage.class);


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
                                        progressBar.setVisibility(View.GONE);
                                        Name.clear();
                                        Price.clear();
                                        F4.clear();
                                        F3.clear();
                                        F2.clear();
                                        F1.clear();
                                        Id.clear();
                                        Bank.clear();
                                        logo.clear();
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
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                    requestQueue.add(jsonobjectRequest);
                }
            }
        });

//        temp1 = findViewById(R.id.temp1);
//        temp1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,cart_orders.class));
//            }
//        });
        return root;
    }
}