package com.foodies.amatfoodies.activitiesAndFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.databinding.LayoutLoginBinding
import com.foodies.amatfoodies.utils.FormLabelTextView
import com.foodies.amatfoodies.utils.TextFormFieldLayout
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment

class LoginActivity : RootFragment() {
    private lateinit var sPref: SharedPreferences
    private lateinit var binding: LayoutLoginBinding
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutLoginBinding.inflate(inflater, container, false)
        mContext = requireContext()
        sPref = mContext.getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE)

        setupUI()

        return binding.root
    }

    private fun setupUI() {
        val txt = "Don’t have an account? <span style='color:#E93821;'>Create Account</span>"
        binding.navigateToSignUp.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
                txt,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ) else Html.fromHtml(txt)

        binding.navigateToSignUp.setOnClickListener {
            switchFragment(R.id.login_root_layout, SignUpActivity())
        }

        binding.backIcon.setOnClickListener {
            try {
                (mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity?.currentFocus?.windowToken, 0
                )
            } catch (e: Exception) {
            }
            activity?.onBackPressed()
        }
        
        binding.navigateForgotPassword.setOnClickListener {
            switchFragment(R.id.login_root_layout, ForgotPasswordFragment())
        }

//        binding.emailAddressInput.validate(
//            binding.emailAddressLabel,
//            true,
//            arrayListOf("Field is required!", "Invalid email address")
//        )

        binding.login.setOnClickListener { }
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

    companion object {
        public val LOGIN_NAME = LoginActivity::class.java.name
    }
}

@JvmOverloads
fun TextFormFieldLayout.validate(
    label: FormLabelTextView,
    validate: Boolean = false,
    errors: ArrayList<String?> = arrayListOf()
) {
    doOnLayout {
        setOnFocusChangeListener { _, _ ->
            Log.i("TextFormFieldLayout", editText?.text.toString())

            if (validate) {
                if (editText?.editableText.isNullOrBlank() || errors.isNotEmpty()) {
                    isErrorEnabled = true
                    error = errors.firstOrNull()
                    setErrorTextColor(
                        AppCompatResources.getColorStateList(context, R.color.error_red)
                    )
                    editText?.apply {
                        textSize = 16F
                        setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.colorBlack,
                                context.theme
                            )
                        )
                    }
                    boxBackgroundColor =
                        ResourcesCompat.getColor(resources, R.color.colorWhite, context.theme)
                    boxStrokeColor =
                        ResourcesCompat.getColor(
                            resources,
                            R.color.primary100,
                            context.theme
                        )
                    boxStrokeWidthFocused = 2
                    boxStrokeErrorColor =
                        AppCompatResources.getColorStateList(context, R.color.deep_red)
                    label.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.primary100,
                            context.theme
                        )
                    )
                } else {
                    boxBackgroundColor =
                        ResourcesCompat.getColor(
                            resources,
                            R.color.colorLightBG,
                            context.theme
                        )
                    boxStrokeColor =
                        ResourcesCompat.getColor(
                            resources,
                            R.color.transparent,
                            context.theme
                        )
                    boxStrokeWidth = 0
                    boxStrokeErrorColor =
                        AppCompatResources.getColorStateList(context, R.color.transparent)
                    label.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.label_color,
                            context.theme
                        )
                    )
                }
            }
        }
    }

}
