package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.models.RestaurantsModel;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;

import java.util.ArrayList;

public class AdapterAllRestaurantData extends RecyclerView.Adapter<AdapterAllRestaurantData.holder> {
    ArrayList<RestaurantsModel> africanCuisine;
    ArrayList<RestaurantsModel> africanWares;
    ArrayList<RestaurantsModel> beautyAndPersonalWares;
    ArrayList<RestaurantsModel> foodsAndGroceries;
    Context context;
    RootFragment activity;
    AdapterImageSlider adapterImageSlider;
    int curPosition;

    public AdapterAllRestaurantData(RootFragment activity, ArrayList<RestaurantsModel> africanCuisine,ArrayList<RestaurantsModel> africanWares,ArrayList<RestaurantsModel> beautyAndPersonalWares,ArrayList<RestaurantsModel> foodsAndGroceries,  Context context) {
        this.africanCuisine = africanCuisine;
        this.africanWares = africanWares;
        this.beautyAndPersonalWares = beautyAndPersonalWares;
        this.foodsAndGroceries = foodsAndGroceries;
        this.context = context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_all_restaurant_data, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        if (position == 0){
            adapterImageSlider = new AdapterImageSlider(activity, africanCuisine, context);
            holder.rv.setAdapter(adapterImageSlider);
            if (africanCuisine.isEmpty()){
                holder.specialty.setText("");
                holder.rv.setVisibility(View.GONE);
            }
            else {
                holder.specialty.setText("African Cuisine");
                holder.rv.setVisibility(View.VISIBLE);
            }
        }
        else if (position == 1){
            adapterImageSlider = new AdapterImageSlider(activity, africanWares, context);
            holder.rv.setAdapter(adapterImageSlider);
            if (africanWares.isEmpty()){
                holder.specialty.setText("");
                holder.rv.setVisibility(View.GONE);
            }
            else {
                holder.specialty.setText("African Wares");
                holder.rv.setVisibility(View.VISIBLE);
            }
        }
        else if (position == 2){
            adapterImageSlider = new AdapterImageSlider(activity, beautyAndPersonalWares, context);
            holder.rv.setAdapter(adapterImageSlider);
            if (beautyAndPersonalWares.isEmpty()){
                holder.specialty.setText("");
                holder.rv.setVisibility(View.GONE);
            }
            else {
                holder.specialty.setText("Beauty and Personal Items");
                holder.rv.setVisibility(View.VISIBLE);
            }
        }
        else if (position == 3){
            adapterImageSlider = new AdapterImageSlider(activity, foodsAndGroceries, context);
            holder.rv.setAdapter(adapterImageSlider);
            if (foodsAndGroceries.isEmpty()){
                holder.specialty.setText("");
                holder.rv.setVisibility(View.GONE);
            }
            else {
                holder.specialty.setText("Food and groceries");
                holder.rv.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.specialty.setText("");
        }
        curPosition = position;

    }
    public void setAfricanCuisine(ArrayList<RestaurantsModel> data){
        africanCuisine.clear();
        africanCuisine.addAll(data);
        africanWares.clear();
        beautyAndPersonalWares.clear();
        foodsAndGroceries.clear();
        notifyDataSetChanged();
    }

    public void setBeautyAndPersonalWares(ArrayList<RestaurantsModel> data){
        africanCuisine.clear();
        africanWares.clear();
        beautyAndPersonalWares.clear();
        beautyAndPersonalWares.addAll(data);
        foodsAndGroceries.clear();
        notifyDataSetChanged();
        //notify();
    }
    public void setAfricanWares(ArrayList<RestaurantsModel> data){
        africanWares.clear();
        africanWares.addAll(data);
        africanCuisine.clear();
        beautyAndPersonalWares.clear();
        foodsAndGroceries.clear();
        notifyDataSetChanged();
        //notify();
    }
    public void setFoodsAndGroceries(ArrayList<RestaurantsModel> data){
        africanCuisine.clear();
        africanWares.clear();
        beautyAndPersonalWares.clear();
        foodsAndGroceries.clear();
        foodsAndGroceries.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class holder extends RecyclerView.ViewHolder{
        RecyclerView rv;
        TextView specialty;
       // TextView noData;
        public holder(@NonNull View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.restaurantRv);
            specialty = itemView.findViewById(R.id.specailty_tv);
            //noData = itemView.findViewById(R.id.specailty_no_data);
        }
    }
}
