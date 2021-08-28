package com.foodies.amatfoodies.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.activities.BaseActivity
import com.foodies.amatfoodies.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mContext = requireContext()

        val toolbar = binding.homeFragmentToolbar
        (requireActivity() as BaseActivity).setSupportActionBar(toolbar)

        return binding.root
    }
}