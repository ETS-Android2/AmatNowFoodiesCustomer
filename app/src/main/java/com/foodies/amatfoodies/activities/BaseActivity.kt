package com.foodies.amatfoodies.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.databinding.ActivityBaseBinding
import com.foodies.amatfoodies.fragments.AccountFragmentDirections
import com.foodies.amatfoodies.fragments.ExploreFragmentDirections
import com.foodies.amatfoodies.fragments.HomeFragmentDirections
import com.foodies.amatfoodies.utils.ContextWrapper
import java.util.*


class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var context: Context
    private lateinit var destinationChangedListener: NavController.OnDestinationChangedListener
    private lateinit var drawerToggle: ActionBarDrawerToggle

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
            supportFragmentManager.findFragmentById(R.id.base_activity_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.baseActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.sideNavView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.sideMenuHome, R.id.sideMenuExplore, R.id.sideMenuAccount),
            binding.drawerLayout,
        )

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.baseActivityToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        NavigationUI.setupWithNavController(
            binding.baseActivityToolbar, navController, appBarConfiguration
        )
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)

        binding.drawerLayout.addDrawerListener(drawerToggle)

        destinationChangedListener =
            NavController.OnDestinationChangedListener { controller, destination, _ ->
                when (destination.id) {
                    R.id.sideMenuHome -> {
                        controller.navigate(
                            HomeFragmentDirections.actionSideMenuHomeToCartFragment()
                        )
                    }
                    R.id.sideMenuExplore -> {
                        controller.navigate(
                            ExploreFragmentDirections.actionSideMenuExploreToExploreFragment()
                        )
                    }
                    R.id.sideMenuAccount -> {
                        controller.navigate(
                            AccountFragmentDirections.actionSideMenuAccountToAccountFragment()
                        )
                    }
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        findNavController(R.id.base_activity_nav_host).navigateUp()
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.base_activity_nav_host).navigateUp(appBarConfiguration)
    }

}