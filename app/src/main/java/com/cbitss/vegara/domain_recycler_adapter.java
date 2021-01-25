package com.cbitss.vegara;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class domain_recycler_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> Domain_name = new ArrayList<>();
    ArrayList<String> Pakage_name = new ArrayList<>();
    ArrayList<String> Due_date = new ArrayList<>();
    ArrayList<String> Days_remaining = new ArrayList<>();
    ArrayList<String> Renew_link = new ArrayList<>();

    geturl x;
    Activity act;

    public domain_recycler_adapter(Context context, ArrayList<String> domain_name, ArrayList<String> pakage_name, ArrayList<String> due_date, ArrayList<String> days_remaining, ArrayList<String> renew_link, geturl x) {
        this.context = context;
        Domain_name = domain_name;
        Pakage_name = pakage_name;
        Due_date = due_date;
        Days_remaining = days_remaining;
        Renew_link = renew_link;
        this.x = x;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_service_item,parent,false);
        return new domain_recycler_adapter.viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        domain_recycler_adapter.viewHolder itemViewHolder = (domain_recycler_adapter.viewHolder)holder;
        itemViewHolder.bank_name.setText(Pakage_name.get(position));
        itemViewHolder.domain.setText(Domain_name.get(position));
        itemViewHolder.type.setText("Due Date : "+Due_date.get(position));
        itemViewHolder.price.setText(Days_remaining.get(position)+" Days Remaining");
        Log.d("check","onbind");
        itemViewHolder.visit.setText("Visit Website");
        itemViewHolder.add.setText("ADD DAYS");
        itemViewHolder.visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Website will be visited", Toast.LENGTH_SHORT).show();
            }
        });
        itemViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Renew_link.get(position)));
                context.startActivity(browserIntent);
                Toast.makeText(context, "Add days", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("item_count",""+Domain_name.size());
        return Domain_name.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView bank_name,domain,type,price;
        Button visit,add;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            bank_name = itemView.findViewById(R.id.bank_name);
            domain = itemView.findViewById(R.id.domain_name);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);
            visit = itemView.findViewById(R.id.confirm);
            add = itemView.findViewById(R.id.cancel);
        }
    }
}
