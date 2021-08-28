package com.foodies.amatfoodies.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.databinding.ActivityBaseBinding
import com.foodies.amatfoodies.utils.ContextWrapper
import java.util.*


class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base)

        sharedPreferences = getSharedPreferences(PreferenceClass.user, MODE_PRIVATE)
        context = this@BaseActivity

        setupUI()

        val languageArray = context.resources.getStringArray(R.array.language_code)
        val languageCode = mutableListOf(*languageArray)
        sharedPreferences = context.getSharedPreferences(PreferenceClass.user, MODE_PRIVATE)
        PreferenceClass.sharedPreferences = sharedPreferences
        val language = sharedPreferences.getString(PreferenceClass.selected_language, "")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && languageCode.contains(language)) {
            val newLocale = Locale(language)
            context = ContextWrapper.wrap(context, newLocale)
        }
    }

    private fun setupUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.baseActivityNavHost) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        val drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.sideMenuHome, R.id.sideMenuExplore, R.id.sideMenuAccount),
            drawerLayout,
        )

        binding.sideNavigationView.setupWithNavController(navController)

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) ||
                super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.baseActivityNavHost).navigateUp(appBarConfiguration)
    }
}