package com.cbitss.vegara;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class bank_recycler_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<String> Bank = new ArrayList<>();
    ArrayList<String> Logo;
    int TYPE_FOOTER = 1;
    String user_bank;
    geturl x;
    ItemViewHolder temp=null;

    public bank_recycler_adapter(Context context, ArrayList<String> bank, ArrayList<String> Logo ,geturl x) {
        this.context = context;
        Bank = bank;
        this.x = x;
        this.Logo = Logo;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == Bank.size())
        {
            return TYPE_FOOTER;
        }
        else
            return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_FOOTER){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pakage_button,parent,false);
            return new FotterViewHolder(itemView);
        }
        else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bank_item,parent,false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            final ItemViewHolder ItemHolder = (ItemViewHolder) holder;

            Glide.with(context)
                    .asBitmap()
                    .load(Logo.get(position))
                    .into(ItemHolder.bank_name);

//            ItemHolder.bank_name.setText(Bank.get(position));
            ItemHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user_bank = Bank.get(position);
                    if (temp != null) {
                        temp.background.setAlpha((float) 0.4);
                    }
                    ItemHolder.background.setAlpha(1);
                    temp = ItemHolder;
                    Toast.makeText(context, user_bank, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(holder instanceof FotterViewHolder){
            final FotterViewHolder ButtonHolder = (FotterViewHolder) holder;
            final Intent i = new Intent(context,payment_details.class);
            ButtonHolder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo api request for details
                    if(user_bank == null){
                        Toast.makeText(context, "Please select the bank", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ButtonHolder.progressBar.setVisibility(View.VISIBLE);
                        final String url = x.placeorderurl(user_bank);
                        Log.d("url",url);
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.getCache().clear();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                try {
                                    Log.d("response",response.getString("package_price")+response.getString("bank_name")+response.getString("bank_account_name")
                                    );
                                    Log.d("response1",response.toString());
                                    i.putExtra("geturl",x);
                                    i.putExtra("id",response.getString("order_id"));
                                    i.putExtra("amount",response.getString("package_price"));
                                    i.putExtra("bank_name",response.getString("bank_name"));
                                    i.putExtra("name",response.getString("bank_account_name"));
                                    i.putExtra("Acc_no",response.getString("bank_account_no"));
                                    i.putExtra("logo",response.getString("bank_logo_url"));
                                    context.startActivity(i);
                                    ButtonHolder.progressBar.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Some Error occurred", Toast.LENGTH_SHORT).show();
                                    Log.d("response_error",e.getMessage());
                                    ButtonHolder.progressBar.setVisibility(View.GONE);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Some Problem occurred", Toast.LENGTH_SHORT).show();
                                ButtonHolder.progressBar.setVisibility(View.GONE);
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Bank.size()+1;
    }

    public class FotterViewHolder extends RecyclerView.ViewHolder {
        Button next;
        ProgressBar progressBar;
        public FotterViewHolder(@NonNull View itemView) {
            super(itemView);
            next = itemView.findViewById(R.id.next);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView bank_name;
        ConstraintLayout item;
        LinearLayout background;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bank_name = itemView.findViewById(R.id.bank_name);
            item = itemView.findViewById(R.id.bank_item);
            background = itemView.findViewById(R.id.bank_background);
        }
    }
}
