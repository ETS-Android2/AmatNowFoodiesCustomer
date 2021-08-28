package com.foodies.amatfoodies.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.facebook.login.LoginFragment
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.databinding.ForgotPasswordDialogFragmentBinding

class ForgotPasswordDialogFragment : DialogFragment() {
    private lateinit var binding: ForgotPasswordDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_dialog)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.forgot_password_dialog_fragment,
            container,
            false
        )

        setupUI()

        return binding.root
    }

    private fun setupUI() {
        binding.resetPasswordBtn.setOnClickListener {
            OperationSuccessfulDialog.newInstance(
                resources.getString(R.string.password_rest_successfully),
                resources.getString(R.string.login)
            ) { childFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) }
                .show(
                    requireActivity().supportFragmentManager,
                    "OperationSuccessfulDialog"
                )
            dismiss()
        }
        binding.closeDialog.setOnClickListener { dismiss() }
    }

    private fun switchFragment(@IdRes layout: Int, next: Fragment) {
        try {
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

            when {
                childFragmentManager.findFragmentById(layout) == null -> transaction.add(
                    layout,
                    next,
                    "parent"
                )
                else -> transaction.replace(layout, next, "parent")
            }

            transaction.addToBackStack(null)
            transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}