package com.cbitss.vegara.ui.gallery;

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
import androidx.fragment.app.FragmentTransaction;
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
import com.cbitss.vegara.MainActivity;
import com.cbitss.vegara.R;
import com.cbitss.vegara.cart_orders;
import com.cbitss.vegara.empty_cart;
import com.cbitss.vegara.geturl;
import com.cbitss.vegara.orders_recycler_adapter;
import com.cbitss.vegara.recycler_View_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    ArrayList<String> Id = new ArrayList<>();
    ArrayList<String> Domain_name = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<String> Bank_name = new ArrayList<>();
    ArrayList<String> Account_name = new ArrayList<>();
    ArrayList<String> Account_no = new ArrayList<>();
    ArrayList<String> Pakage_name = new ArrayList<>();
    public RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


//          startActivity(new Intent(getActivity(),cart_orders.class));
        SharedPreferences shared = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        String Phone = shared.getString("phone",null);
        String id = shared.getString("id",null);
//
        final geturl x = new geturl(Phone,id);
        final String url = x.carturl();

        Log.d("url_cart",url);
        Log.d("debug_arrraylist",Id.toString()+Domain_name.toString()+Price.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        Id.clear();
        Domain_name.clear();;
        Price.clear();
        Bank_name.clear();
        Account_name.clear();
        Account_no.clear();
//        Pakage_name.clear();
        Log.d("status","request queue created");
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
                        startActivity(new Intent(getActivity(), empty_cart.class));
                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction trans = manager.beginTransaction();
////                        trans.remove(GalleryFragment);
////                        trans.commit();
////                        manager.popBackStack();
                        manager.popBackStack();
                    }
                    else {
                        JSONArray obj = response.getJSONArray("order");
                        for (int i=0;i<obj.length();i++){
                            JSONObject order = obj.getJSONObject(i);
                            Log.d("whole order json",order.toString());
                            Id.add(order.getString("order_id"));
                            Domain_name.add(order.getString("domain_name"));
                            Pakage_name.add(order.getString("package_type"));
                            Price.add(order.getString("package_price"));
                            Bank_name.add(order.getString("bank_name"));
                            Account_name.add(order.getString("bank_account_name"));
                            Account_no.add(order.getString("bank_account_no"));
                            Log.d("temp_url",x.deleteorderurl(order.getString("domain_name"),order.getString("order_id")));
                            Log.d("debug_arrraylist_update",Id.toString()+Domain_name.toString()+Price.toString());

                        }
                        Log.d("data is",Id.toString());
                        recyclerView = root.findViewById(R.id.orders_list);
                        orders_recycler_adapter adapter = new orders_recycler_adapter(getActivity(),Id,Domain_name,Pakage_name,Price,Bank_name,Account_name,Account_no,x);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
//                    else
//                        Toast.makeText(getActivity(), ""+status1.get("reason"), Toast.LENGTH_SHORT).show();
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
    public void restart(){}
//    private void fetchdata() {
//
//    }
}