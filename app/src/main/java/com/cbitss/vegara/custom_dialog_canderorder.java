package com.cbitss.vegara;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cbitss.vegara.ui.gallery.GalleryFragment;
import com.cbitss.vegara.ui.slideshow.SlideshowFragment;

class custom_dialog_canderorder extends Dialog implements android.view.View.OnClickListener {
    public Activity c;
    public Dialog d;
    public Button yes, no;
    public TextView domain,price,type;
    String Domain,Price,Type,Id;
    geturl x;

    public custom_dialog_canderorder(@NonNull Context context,String domain,String price , String type , geturl x , String id) {
        super(context);
        this.c = c;
        Domain = domain;
        Price = price;
        Type = type;
        Id = id;
        this.x = x;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cancel_confirm_dialog);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        domain = findViewById(R.id.domain);
        price = findViewById(R.id.price);
        type = findViewById(R.id.type);
        domain.setText(Domain);
        price.setText(Price);
        type.setText(Type);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                String url = x.deleteorderurl(Domain,Id);
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.getCache().clear();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rsponse_dialogue",response);
                        if (response.equals("ok"))
                        {
                            Toast.makeText(getContext(), "Order deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals("error"))
                            Toast.makeText(getContext(), "Failed to cancel the order", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Failed to cancel the order", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequest);
                dismiss();
//                c.finish();
                Intent i = new Intent(getContext(),MainActivity.class);
                getContext().startActivity(i);
//                c.restart();
//                restart();
//                c.finish();
                break;
            case R.id.no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
