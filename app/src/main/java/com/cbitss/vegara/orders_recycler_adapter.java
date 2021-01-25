package com.cbitss.vegara;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cbitss.vegara.ui.slideshow.SlideshowFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class orders_recycler_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<String> Id = new ArrayList<>();
    ArrayList<String> Domain_name = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<String> Bank_name = new ArrayList<>();
    ArrayList<String> Account_name = new ArrayList<>();
    ArrayList<String> Account_no = new ArrayList<>();
    ArrayList<String> Pakage_name = new ArrayList<>();
    geturl x;
    Activity act;
    Context context;

    public orders_recycler_adapter(Context context,ArrayList<String> id, ArrayList<String> domain_name, ArrayList<String> package_name , ArrayList<String> price, ArrayList<String> bank_name, ArrayList<String> account_name, ArrayList<String> account_no , geturl x) {
        this.context = context;
        Id = id;
        Domain_name = domain_name;
        Pakage_name = package_name;
        Price = price;
        Bank_name = bank_name;
        Account_name = account_name;
        Account_no = account_no;
        this.x = x;
        this.act = act;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orders_item,parent,false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        viewHolder itemViewHolder = (viewHolder)holder;
        itemViewHolder.bank_name.setText(Bank_name.get(position));
        itemViewHolder.type.setText(Pakage_name.get(position));
        itemViewHolder.price.setText(Price.get(position));
        itemViewHolder.domain.setText(Domain_name.get(position));
        Log.d("check","onbind");
        itemViewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,payment_details.class);
                i.putExtra("amount",Price.get(position));
                i.putExtra("bank_name",Bank_name.get(position));
                i.putExtra("name",Account_name.get(position));
                i.putExtra("Acc_no",Account_no.get(position));
                i.putExtra("geturl",x);
                x.setDomain(Domain_name.get(position));
                i.putExtra("id",Id.get(position));
                context.startActivity(i);

            }
        });
        itemViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "cancel clicked at "+position, Toast.LENGTH_SHORT).show();
                custom_dialog_canderorder confirm = new custom_dialog_canderorder(context,Domain_name.get(position),Price.get(position),Pakage_name.get(position),x,Id.get(position));
                confirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Log.d("before opening","dialogue box");
                confirm.show();
                Log.d("after","opening dialogue box");
//                restart();
                Log.d("after","calling restart");

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("item_count",""+Id.size());
        return Id.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView bank_name,domain,type,price;
        Button confirm,cancel;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            bank_name = itemView.findViewById(R.id.bank_name);
            domain = itemView.findViewById(R.id.domain_name);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);
            confirm = itemView.findViewById(R.id.confirm);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
    private void restart(){


        final String url;
        url = x.carturl();

        Log.d("url_cart",url);
        Log.d("debug_arrraylist",Id.toString()+Domain_name.toString()+Price.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        Id.clear();
        Domain_name.clear();;
        Price.clear();
        Bank_name.clear();
        Account_name.clear();
        Account_no.clear();
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
                        context.startActivity(new Intent(context, empty_cart.class));
//                        FragmentManager manager = context.getSupportFragmentManager();
//                        FragmentTransaction trans = manager.beginTransaction();
////                        trans.remove(GalleryFragment);
////                        trans.commit();
////                        manager.popBackStack();
//                        manager.popBackStack();
                    }
                    else{
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
