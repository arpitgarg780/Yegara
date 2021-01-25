package com.cbitss.vegara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class pakage extends AppCompatActivity{//} implements Serializable {

    private ArrayList<String> Name = new ArrayList<>();
    private ArrayList<String> Price = new ArrayList<>();
    private ArrayList<String> F1 = new ArrayList<>();
    private ArrayList<String> F2 = new ArrayList<>();
    private ArrayList<String> F3 = new ArrayList<>();
    private ArrayList<String> F4 = new ArrayList<>();
    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> Bank = new ArrayList<>();
    private ArrayList<String> logo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakage);

        Name =  getIntent().getStringArrayListExtra("name");
        Price = getIntent().getStringArrayListExtra("price");
        F1 = getIntent().getStringArrayListExtra("f1");
        F2 = getIntent().getStringArrayListExtra("f2");
        F3 = getIntent().getStringArrayListExtra("f3");
        F4 = getIntent().getStringArrayListExtra("f4");
        Id = getIntent().getStringArrayListExtra("id");
        Bank = getIntent().getStringArrayListExtra("bank");
        logo = getIntent().getStringArrayListExtra("logo");
        geturl x = (geturl) getIntent().getSerializableExtra("geturl");
        Log.d("recycler","recycler view init");
        RecyclerView recyclerView = findViewById(R.id.list_package);
        recycler_View_adapter adapter = new recycler_View_adapter(this,Name,Price,F1,F2,F3,F4,Id,Bank,logo,x);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}