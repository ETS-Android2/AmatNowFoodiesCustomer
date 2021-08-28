package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.models.AppImageSlider;

import java.util.ArrayList;

import view.GetTargetView;

public class AppImageSliderAdapter extends RecyclerView.Adapter<AppImageSliderAdapter.MyViewHolder> {

    private final ArrayList<AppImageSlider> sliders;
    private final Context context;
    private final GetTargetView getTargetView;

    public AppImageSliderAdapter(ArrayList<AppImageSlider> sliders, Context context, GetTargetView getTargetView) {
        this.sliders = sliders;
        this.context = context;
        this.getTargetView = getTargetView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppImageSliderAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_app_image_slider_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AppImageSlider slider = sliders.get(position);

        if (slider.getImage() != null) {
            /*RequestOptions options = RequestOptions.fitCenterTransform()
                    .transform(new RoundedCorners(5))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(holder.imageView.getWidth(), holder.imageView.getHeight());*/

            Glide.with(context).asBitmap().thumbnail(0.03f).load(Config.imgBaseURL + slider.getImage())
                    .placeholder(R.drawable.image_placeholder).into(holder.imageView);

        } else
            Glide.with(context).clear(holder.imageView);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetView.checkSlider(slider);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sliders.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView parentLayout;

        public MyViewHolder(@NonNull View view) {
            super(view);

            imageView = view.findViewById(R.id.app_slider_image_view);
            parentLayout = view.findViewById(R.id.app_slider_layout);


        }

    }
}
