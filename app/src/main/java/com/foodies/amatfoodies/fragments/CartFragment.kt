package com.foodies.amatfoodies.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.databinding.FragmentCartBinding
import com.foodies.amatfoodies.databinding.FragmentHomeBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        mContext = requireContext()

        return binding.root
    }
}