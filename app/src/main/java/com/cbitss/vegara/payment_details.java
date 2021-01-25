package com.cbitss.vegara;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class payment_details extends AppCompatActivity {
    TextView amount,bank_name,name,Acc_no,domain;
    EditText depositer_name,reference_no,date;
    ImageView bank_logo;
    Button confirm;
    String orderid;
    geturl x;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        amount = findViewById(R.id.amount);
        bank_name = findViewById(R.id.bank_name);
        name = findViewById(R.id.name);
        Acc_no = findViewById(R.id.account_no);
        domain = findViewById(R.id.domain);
        depositer_name = findViewById(R.id.depositor_name);
        reference_no = findViewById(R.id.reference_no);
        date = findViewById(R.id.deposit_date);
        confirm = findViewById(R.id.confirm);
        bank_logo = findViewById(R.id.bank_logo);
        progressBar = findViewById(R.id.progressBar);

        Intent i = getIntent();
            amount.setText(i.getStringExtra("amount"));
            bank_name.setText(i.getStringExtra("bank_name"));
            name.setText(i.getStringExtra("name"));
            Acc_no.setText(i.getStringExtra("Acc_no"));
            x = (geturl) i.getSerializableExtra("geturl");
            domain.setText(x.getDomain());
            orderid = i.getStringExtra("id");

        Glide.with(payment_details.this)
                .asBitmap()
                .load(i.getStringExtra("logo"))
                .into(bank_logo);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int flag = 0;
                    String Name = depositer_name.getText().toString();
                    final String reference = reference_no.getText().toString();
                    String Date = date.getText().toString();
                    if(TextUtils.isEmpty(Name)){
                        name.setError("Depositor name is required");
                        flag = 1;
                    }
                    if (TextUtils.isEmpty(reference)){
                        reference_no.setError("Transaction reference number is required");
                        flag = 1;
                    }
                    if(TextUtils.isEmpty(Date)){
                        date.setError("Date is required");
                    }
                    else if(flag == 0){
                        progressBar.setVisibility(View.VISIBLE);
                        final String url = x.confirmorderurl(orderid,Name,reference,Date);
                        Log.d("confirm url",url);
                        RequestQueue requestQueue = Volley.newRequestQueue(payment_details.this);
//                        requestQueue.getCache().clear();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("url",url);
                                Log.d("response is ",response);
                                if(response.equals("ok")){
                                    startActivity(new Intent(payment_details.this,success.class));
                                }
                                else {
                                    Log.d("response_success_error",response);
                                    Toast.makeText(payment_details.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                Log.d("response_error",error.getMessage());
                                Toast.makeText(payment_details.this, "Some problem occurred", Toast.LENGTH_SHORT).show();
                                Log.d("response",error.toString());
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        requestQueue.add(stringRequest);
                        startActivity(new Intent(payment_details.this,success.class));
                    }

                }
            });
    }
}