package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.foodies.amatfoodies.constants.ApiRequest;
import com.foodies.amatfoodies.constants.Callback;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.constants.Functions;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.models.RestaurantsModel;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterImageSlider extends RecyclerView.Adapter<AdapterImageSlider.holder> {
    ArrayList<RestaurantsModel> data;
    Context context;
    RootFragment activity;
    SharedPreferences sharedPreferences;

    public AdapterImageSlider(RootFragment activity, ArrayList<RestaurantsModel> data, Context context) {
        this.data = data;
        this.context = context;
        this.activity = activity;
        sharedPreferences = context.getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AdapterImageSlider.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_single_restaurant, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImageSlider.holder holder, int position) {
        holder.retaurant_name.setText(data.get(position).restaurant_name);
        holder.description.setText(data.get(position).restaurant_about);
        holder.item_delivery_time_tv.setText(data.get(position).deliveryTime);
        Uri uri = Uri.parse(Config.imgBaseURL + data.get(position).restaurant_image);

        RequestOptions options = RequestOptions.fitCenterTransform()
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(holder.restaurant_image.getWidth(), holder.restaurant_image.getHeight());

        Glide.with(context).asBitmap().thumbnail(0.03f).apply(options).load(Config.imgBaseURL + data.get(position).restaurant_image)
                .placeholder(R.drawable.image_placeholder).into(holder.restaurant_image);

        //holder.restaurant_image.setImageURI(uri);
        holder.ratingBar.setRating(Float.parseFloat(data.get(position).restaurant_avgRating));
        holder.baked_time.setText(data.get(position).preparation_time);
        holder.favorite.setTag(data.get(position));
        holder.kilometer.setText(data.get(position).restaurant_distance);
        RestaurantsModel checkWetherToShow = (RestaurantsModel) holder.favorite.getTag();

        if (checkWetherToShow.restaurant_isFav.equalsIgnoreCase("1")) {
            holder.favorite.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24_blue);
        }

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

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWetherToShow.restaurant_isFav.equalsIgnoreCase("1")) {
                    holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24_blue);
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_heart_filled);
                }
                addFavoriteRestaurant(position, data.get(position).restaurant_id);
            }
        });
    }

    public void addFavoriteRestaurant(int pos, String res_id) {

        final String user_id = sharedPreferences.getString(PreferenceClass.pre_user_id, "");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("restaurant_id", res_id);
            jsonObject.put("favourite", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.showLoader(context, false, false);
        ApiRequest.callApi(context, Config.ADD_FAV_RESTAURANT, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                Functions.cancelLoader();

                try {
                    JSONObject converResponseToJson = new JSONObject(resp);

                    int code_id = Integer.parseInt(converResponseToJson.optString("code"));
                    if (code_id == 200) {
                        RestaurantsModel item = data.get(pos);
                        if (item.restaurant_isFav.equals("0")) {
                            item.restaurant_isFav = "1";
                        } else {
                            item.restaurant_isFav = "0";
                        }
                        data.remove(pos);
                        data.add(pos, item);
                        notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder {
        ImageView restaurant_image;
        ImageButton favorite;

        TextView retaurant_name, description, item_delivery_time_tv, baked_time, kilometer;
        RatingBar ratingBar;

        public holder(@NonNull View itemView) {
            super(itemView);
            kilometer = itemView.findViewById(R.id.Kilometer);
            restaurant_image = itemView.findViewById(R.id.restaurant_image);
            retaurant_name = itemView.findViewById(R.id.restaurant_name);
            description = itemView.findViewById(R.id.description);
            item_delivery_time_tv = itemView.findViewById(R.id.item_delivery_time_tv);
            ratingBar = itemView.findViewById(R.id.ruleRatingBar);
            favorite = itemView.findViewById(R.id.favourite);
            baked_time = itemView.findViewById(R.id.baked_time_tv);
        }
    }
}
