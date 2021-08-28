package com.foodies.amatfoodies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.models.RestaurantsModel;
import com.foodies.amatfoodies.models.SpecialtyRestuarantsModel;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;

import java.util.ArrayList;

public class AdapterAllRestaurantData2 extends RecyclerView.Adapter<AdapterAllRestaurantData2.holder> {

    ArrayList<SpecialtyRestuarantsModel> list;
    Context context;
    RootFragment activity;
    AdapterImageSlider adapterImageSlider;
    int curPosition;

    public AdapterAllRestaurantData2(RootFragment activity, ArrayList<SpecialtyRestuarantsModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_all_restaurant_data, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        SpecialtyRestuarantsModel model = list.get(position);

        holder.rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapterImageSlider = new AdapterImageSlider(activity, model.getList(), context);
        holder.rv.setAdapter(adapterImageSlider);
        adapterImageSlider.notifyDataSetChanged();
        holder.specialty.setText(model.getName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class holder extends RecyclerView.ViewHolder {
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
