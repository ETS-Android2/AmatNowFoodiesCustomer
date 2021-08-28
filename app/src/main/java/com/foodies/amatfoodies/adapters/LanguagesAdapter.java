package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.constants.AdapterClickListener;
import com.foodies.amatfoodies.models.Languages_Model;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.viewHolders.LanguageViewHolder;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by foodies on 10/18/2019.
 */

public class LanguagesAdapter extends RecyclerView.Adapter<LanguageViewHolder>  {

    ArrayList<Languages_Model> dataList;
    Context context;
    AdapterClickListener adapterClickListener;


    public LanguagesAdapter(Context context, ArrayList<Languages_Model> dataList, AdapterClickListener listener){
        this.dataList = dataList;
        this.context = context;
        this.adapterClickListener =listener;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_languages_layout, parent, false);

        LanguageViewHolder viewHolder = new LanguageViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, final int position) {
        Languages_Model item =  dataList.get(position);
        holder.countryIcon.setImageResource(context.getResources().getIdentifier("flag_" +item.code.toLowerCase(Locale.ENGLISH), "drawable", context.getPackageName()));
        holder.languageTitle.setText(item.name);
        holder.languageNameTxt.setText(item.language);

        holder.OnBind(position,item, adapterClickListener);

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }




}
