package com.foodies.amatfoodies.activitiesAndFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.databinding.LayoutSignupBinding
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment

class SignUpActivity : RootFragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: LayoutSignupBinding
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_signup, container, false)
        mContext = requireContext()
        prefs = mContext.getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE)

        setupUI()

        return binding.root
    }

    fun setupUI() {
        val txt = "Already have an account? <span style='color:#E93821;'>Log In</span>"
        binding.navigateToLogin.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
                txt,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ) else Html.fromHtml(txt)

        val toolbar = binding.signupActivityToolbar
        toolbar.title = ""
        toolbar.subtitle = ""
        toolbar.setNavigationOnClickListener {
            (mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken, 0
            )
        }
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        binding.navigateToLogin.setOnClickListener {
            switchFragment(R.id.signup_root_layout, LoginActivity())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.back_icon -> {
                Toast.makeText(mContext, "Backarrow pressed", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }


    private fun switchFragment(@IdRes layout: Int, next: Fragment) {
        try {
            val transaction = childFragmentManager.beginTransaction()
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
}