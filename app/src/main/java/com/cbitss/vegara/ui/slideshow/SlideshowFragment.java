package com.cbitss.vegara.ui.slideshow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cbitss.vegara.R;
import com.cbitss.vegara.domain_recycler_adapter;
import com.cbitss.vegara.empty_cart;
import com.cbitss.vegara.geturl;
import com.cbitss.vegara.orders_recycler_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    ArrayList<String> Domain_name = new ArrayList<>();
    ArrayList<String> Due_date = new ArrayList<>();
    ArrayList<String> Days_remaining = new ArrayList<>();
    ArrayList<String> Renew_link = new ArrayList<>();
    ArrayList<String> Pakage_name = new ArrayList<>();
    public RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        SharedPreferences shared = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        String Phone = shared.getString("phone",null);
        String id = shared.getString("id",null);
//
        final geturl x = new geturl(Phone,id);
        final String url = x.serviceurl();

        Log.d("url_service",url);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        Domain_name.clear();
        Due_date.clear();
        Days_remaining.clear();
        Renew_link.clear();
        Pakage_name.clear();
        Log.d("status","request queue created");
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("url_service_check",url);
                    Log.d("service response",response.toString());
                    JSONArray status = response.getJSONArray("status");
                    JSONObject status1 = status.getJSONObject(0);
                    Log.d("status",status1.getString("status"));
                    if(status1.getString("status").equals("empty")){
                        startActivity(new Intent(getActivity(), empty_cart.class));
                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction trans = manager.beginTransaction();
////                        trans.remove(GalleryFragment);
////                        trans.commit();
////                        manager.popBackStack();
                        manager.popBackStack();
                    }
                    else if(status1.equals("full")){
                        JSONArray obj = response.getJSONArray("service");
                        for (int i=0;i<obj.length();i++){
                            JSONObject order = obj.getJSONObject(i);
                            Log.d("whole order json",order.toString());
                            Domain_name.add(order.getString("domain_name"));
                            Pakage_name.add(order.getString("package_name"));
                            Due_date.add(order.getString("due_date"));
                            Days_remaining.add(order.getString("days_remaining"));
                            Renew_link.add(order.getString("renew_link"));

                        }
                        recyclerView = root.findViewById(R.id.orders_list);
                        domain_recycler_adapter adapter = new domain_recycler_adapter(getActivity(),Domain_name,Pakage_name,Due_date,Days_remaining,Renew_link,x);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
                    else
                        Toast.makeText(getActivity(), ""+status1.get("reason"), Toast.LENGTH_SHORT).show();
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

        return root;
    }
}