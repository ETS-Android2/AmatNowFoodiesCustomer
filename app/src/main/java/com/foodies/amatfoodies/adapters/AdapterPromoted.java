package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.foodies.amatfoodies.activitiesAndFragments.RestaurantMenuItemsFragment;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.models.RestaurantsModel;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;
import com.foodies.amatfoodies.databinding.ItemPromotedDashboardBinding;

import java.util.ArrayList;

public class AdapterPromoted extends RecyclerView.Adapter<AdapterPromoted.holder> {
    ItemPromotedDashboardBinding binding;
    Context context;
    ArrayList<RestaurantsModel> data;
    RootFragment activity;

    public AdapterPromoted(RootFragment activity, Context context, ArrayList<RestaurantsModel> data) {
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_promoted_dashboard, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.title_restaurant.setText(data.get(position).restaurant_name);
        holder.desciption.setText(data.get(position).restaurant_about);
        holder.distant_restaurant.setText(data.get(position).restaurant_distance);
        holder.baked_time.setText(data.get(position).preparation_time);
        holder.item_delivery_time.setText(data.get(position).deliveryTime);
        Uri uri = Uri.parse(Config.imgBaseURL + data.get(position).restaurant_image);
        RequestOptions options = RequestOptions.fitCenterTransform()
                .transform(new RoundedCorners(5))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(holder.profile_image_restaurant.getWidth(), holder.profile_image_restaurant.getHeight());

        Glide.with(context).asBitmap().thumbnail(0.03f).apply(options).load(Config.imgBaseURL + data.get(position).restaurant_image)
                .placeholder(R.drawable.image_placeholder).into(holder.profile_image_restaurant);
        //holder.profile_image_restaurant.setImageURI(uri);
        holder.ratingBar.setRating(Float.parseFloat(data.get(position).restaurant_avgRating));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment restaurantMenuItemsFragment = new RestaurantMenuItemsFragment();
                FragmentTransaction transaction = activity.getChildFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data.get(position));
                restaurantMenuItemsFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.add(R.id.newsFragment, restaurantMenuItemsFragment, "parent").commit();
            }
        });
    }

    public void addItem(ArrayList<RestaurantsModel> data) {
        data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder {
        TextView title_restaurant, desciption, distant_restaurant, baked_time, item_delivery_time;
        ImageView profile_image_restaurant;
        RatingBar ratingBar;

        public holder(@NonNull View itemView) {
            super(itemView);
            title_restaurant = itemView.findViewById(R.id.title_restaurants);
            desciption = itemView.findViewById(R.id.salogon_restaurants);
            distant_restaurant = itemView.findViewById(R.id.distanse_restaurants);
            baked_time = itemView.findViewById(R.id.baked_time_tv);
            item_delivery_time = itemView.findViewById(R.id.item_delivery_time_tv);
            profile_image_restaurant = itemView.findViewById(R.id.profile_image_restaurant);
            ratingBar = itemView.findViewById(R.id.ruleRatingBar);
        }
    }
}
