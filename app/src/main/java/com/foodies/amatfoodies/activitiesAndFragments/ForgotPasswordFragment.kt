package com.foodies.amatfoodies.activitiesAndFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.databinding.ForgotPasswordFragmentBinding
import com.foodies.amatfoodies.dialogs.ForgotPasswordDialogFragment
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment
import com.foodies.amatfoodies.viewModels.ForgotPasswordViewModel

class ForgotPasswordFragment : RootFragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var mContext: Context
    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var binding: ForgotPasswordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.forgot_password_fragment, container, false)
        mContext = requireContext()
        prefs = mContext.getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.forgotPasswordBtn.setOnClickListener {
            val fragment = ForgotPasswordDialogFragment()
            fragment.show(
                requireActivity().supportFragmentManager,
                "ForgotPasswordFragment"
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        // TODO: Use the ViewModel
    }
}