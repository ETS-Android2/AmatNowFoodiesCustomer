package com.foodies.amatfoodies.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.constants.AdapterClickListener;
import com.foodies.amatfoodies.models.Languages_Model;
import com.foodies.amatfoodies.R;

public class LanguageViewHolder  extends RecyclerView.ViewHolder {

    public TextView languageTitle, languageNameTxt;
    public ImageView countryIcon;


    public LanguageViewHolder(View itemView) {

        super(itemView);
        languageTitle = (TextView) itemView.findViewById(R.id.language_title);
        languageNameTxt = (TextView) itemView.findViewById(R.id.language_name_txt);

        countryIcon = (ImageView) itemView.findViewById(R.id.country_icon);

    }

    public void OnBind(int pos, Languages_Model item, AdapterClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, pos, item);
            }
        });
    }
}