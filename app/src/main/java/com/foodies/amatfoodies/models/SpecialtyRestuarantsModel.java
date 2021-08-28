package com.foodies.amatfoodies.models;

import java.util.ArrayList;

public class SpecialtyRestuarantsModel {

    public String getName() {
        return name;
    }

    public ArrayList<RestaurantsModel> getList() {
        return list;
    }

    private String name;
    private ArrayList<RestaurantsModel> list;

    public SpecialtyRestuarantsModel(String name, ArrayList<RestaurantsModel> list){
        this.name = name;
        this.list = list;
    }


}
