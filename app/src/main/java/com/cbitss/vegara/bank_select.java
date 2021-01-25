package com.cbitss.vegara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class bank_select extends AppCompatActivity {
    ArrayList<String> Bank = new ArrayList<>();
    ArrayList<String> Logo = new ArrayList<>();
    geturl x;
    bank_recycler_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_select);
        Bank = getIntent().getStringArrayListExtra("bank");
        x = (geturl) getIntent().getSerializableExtra("geturl");
        Logo = getIntent().getStringArrayListExtra("logo");
        RecyclerView recyclerView = findViewById(R.id.banks);
        adapter = new bank_recycler_adapter(this,Bank,Logo,x);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}