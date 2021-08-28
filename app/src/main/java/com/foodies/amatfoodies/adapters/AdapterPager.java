package com.foodies.amatfoodies.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.foodies.amatfoodies.activitiesAndFragments.NewRestuarantsFragment2;
import com.foodies.amatfoodies.activitiesAndFragments.UserAccountFragment;
import com.foodies.amatfoodies.fragments.CartFragment;
import com.foodies.amatfoodies.activitiesAndFragments.OrdersFragment;

/**
 * Created by foodies on 10/18/2019.
 */

public class AdapterPager extends FragmentPagerAdapter {


    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    String specialty, hasRestaurant;

    public AdapterPager(final Resources resources, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setHasRestaurant(String hasRestaurant) {
        this.hasRestaurant = hasRestaurant;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        switch (position) {

            case 0:
                NewRestuarantsFragment2 frag = new NewRestuarantsFragment2();
                Bundle bundle = new Bundle();
                bundle.putString("search", specialty);
                bundle.putString("hasRestaurant", hasRestaurant);
                frag.setArguments(bundle);
                result = frag;
                break;
            case 1:
                result = new OrdersFragment();
                break;

            case 2:
                result = new CartFragment();
                break;
            case 3:
                result = new UserAccountFragment();
                break;

        }
        return result;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(final int position) {

        return null;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}

