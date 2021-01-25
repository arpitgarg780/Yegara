package com.cbitss.vegara;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycler_View_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> Name = new ArrayList<>();
    private ArrayList<String> Price = new ArrayList<>();
    private ArrayList<String> F1 = new ArrayList<>();
    private ArrayList<String> F2 = new ArrayList<>();
    private ArrayList<String> F3 = new ArrayList<>();
    private ArrayList<String> F4 = new ArrayList<>();
    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> Bank;
    private ArrayList<String> Logo;
    private Context context;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;
    geturl x;
    String user_pakage_id;
    int position_selected;
    ItemViewHolder temp;


    public recycler_View_adapter(Context context,ArrayList<String> Name, ArrayList<String> Price, ArrayList<String> F1, ArrayList<String> F2, ArrayList<String> F3, ArrayList<String> F4,ArrayList<String> Id,ArrayList<String> Bank , ArrayList<String> Logo ,geturl x) {
        this.Name = Name;
        this.Price = Price;
        this.F1 = F1;
        this.F2 = F2;
        this.F3 = F3;
        this.F4 = F4;
        this.Id = Id;
        this.context = context;
        this.Bank = Bank;
        this.x = x;
        this.Logo = Logo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pakage_button, parent, false);
            return new FotterViewHolder(itemView);
        }
        else {
            View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_packageitem,parent,false);
            Log.d("type_view","view is "+viewType);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }
//            else return null;

    }

    @Override
    public int getItemViewType(int position) {
        if(position == Name.size())
            return TYPE_FOOTER;
        else
            return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Log.d("onbindViewHolder","calles");
        if(holder instanceof ItemViewHolder) {
            final ItemViewHolder ItemHolder = (ItemViewHolder) holder;
            ItemHolder.name.setText(Name.get(position));
            ItemHolder.price.setText(Price.get(position));
            ItemHolder.f1.setText(F1.get(position));
            ItemHolder.f2.setText(F2.get(position));
            ItemHolder.f3.setText(F3.get(position));
            ItemHolder.f4.setText(F4.get(position));
            ItemHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (temp!=null){
                        temp.bg.setBackground(getDrawableWithRadius());
                        temp.name.setTextColor(Color.BLACK);
                        temp.price.setTextColor(Color.BLACK);
                        temp.f1.setTextColor(Color.BLACK);
                        temp.f2.setTextColor(Color.BLACK);
                        temp.f3.setTextColor(Color.BLACK);
                        temp.f4.setTextColor(Color.BLACK);
                        temp.currency.setTextColor(Color.BLACK);
                        temp.bg.setBackground(getDrawableWithRadius2());
                    }
                    user_pakage_id = Id.get(position);
                    ItemHolder.bg.setBackground(getDrawableWithRadius());
                    ItemHolder.name.setTextColor(Color.WHITE);
                    ItemHolder.price.setTextColor(Color.WHITE);
                    ItemHolder.f1.setTextColor(Color.WHITE);
                    ItemHolder.f2.setTextColor(Color.WHITE);
                    ItemHolder.f3.setTextColor(Color.WHITE);
                    ItemHolder.f4.setTextColor(Color.WHITE);
                    ItemHolder.currency.setTextColor(Color.WHITE);
                    temp = ItemHolder;

                    Log.d("selected package id", "id "+user_pakage_id);
                }
            });
        }
        else if(holder instanceof FotterViewHolder){
            FotterViewHolder ButtonHolder = (FotterViewHolder) holder;
            ButtonHolder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_pakage_id == null) {
                        Toast.makeText(context, "Please select the package", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(context, bank_select.class);
                        x.setPakage_id(user_pakage_id);
                        x.setPakage_name(Name.get(position_selected));
                        i.putExtra("geturl",x);
                        i.putExtra("bank", Bank);
                        i.putExtra("logo",Logo);
                        context.startActivity(i);
                    }
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return Name.size()+1;
    }
    public class FotterViewHolder extends RecyclerView.ViewHolder {
                Button next;
        public FotterViewHolder(@NonNull View itemView) {
            super(itemView);
            next = itemView.findViewById(R.id.next);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,f1,f2,f3,f4,currency;
        ConstraintLayout item;
        LinearLayout bg;
        ProgressBar progressBar;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            f1 = itemView.findViewById(R.id.f1);
            f2 = itemView.findViewById(R.id.f2);
            f3 = itemView.findViewById(R.id.f3);
            f4 = itemView.findViewById(R.id.f4);
            item = itemView.findViewById(R.id.pakageid);
            bg = itemView.findViewById(R.id.background_color);
            currency = itemView.findViewById(R.id.currency);

        }
    }
    private Drawable getDrawableWithRadius() {

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{100, 100, 100, 100, 100, 100, 100, 100});
        gradientDrawable.setColor(Color.parseColor("#172f53"));
        return gradientDrawable;
    }

    private Drawable getDrawableWithRadius2() {

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{100, 100, 100, 100, 100, 100, 100, 100});
        gradientDrawable.setColor(Color.WHITE);
        return gradientDrawable;
    }
}
