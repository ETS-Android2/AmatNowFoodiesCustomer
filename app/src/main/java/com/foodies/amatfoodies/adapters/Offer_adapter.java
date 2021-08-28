package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.activitiesAndFragments.WebviewActivity;

import java.util.ArrayList;
import java.util.List;


public class Offer_adapter extends RecyclerView.Adapter<Offer_adapter.ViewHolder> {

    private  Context context;
    private final LayoutInflater mInflater;
    private List<String> the_adapter;
    private   List<String> button_names;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Offer_adapter(Context context, List<String> textss, List<String> btn_names) {
        this.mInflater = LayoutInflater.from(context);

        this.context = context;
        this.the_adapter = textss;
        this.button_names = btn_names;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_offer_card, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String texts = the_adapter.get(position);
        String btn = button_names.get(position);
        holder.myTextView.setText(texts);
        holder.button.setText(btn);
        if(position==0){

            holder.imageView.setBackgroundResource(R.drawable.handshake);
        }
        else if(position==1){
            holder.imageView.setBackgroundResource(R.drawable.driver);

        }
        else if(position==2){
            holder.imageView.setBackgroundResource(R.drawable.appviewpic);

        }else if(position==3) {
            holder.imageView.setBackgroundResource(R.drawable.grocery_icon);
        } else{
            holder.imageView.setBackgroundResource(R.drawable.favouritepng);
        }

        ArrayList<String> stringoflinks = new ArrayList<>();
        stringoflinks.add("https://panel.amatnow.com/stores/");
        stringoflinks.add("offer");
        stringoflinks.add("https://amatnow.com/about.html");
        stringoflinks.add("https://amatnow.com/contact.html");
        stringoflinks.add("https://support.amatnow.com/");



        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position != 1) {
                    Intent nextScreen = new Intent(context, WebviewActivity.class);
                    nextScreen.putExtra("URL", stringoflinks.get(position));
                    context.startActivity(nextScreen);
                }
                else {
                    final String appPackageName = "com.foodies.amatrider";
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException e) { // if there is no Google Play on device
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }
        });



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return the_adapter.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myView;
        TextView myTextView;
        ImageView imageView;
        Button button;


        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.Iv_button);
            myTextView = itemView.findViewById(R.id.tvOfferTitle);
            imageView = itemView.findViewById(R.id.ivOffer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return the_adapter.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}