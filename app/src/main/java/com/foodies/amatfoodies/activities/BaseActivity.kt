package com.foodies.amatfoodies.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.AllConstants
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.databinding.ActivityBaseBinding
import com.foodies.amatfoodies.fragments.AccountFragmentDirections
import com.foodies.amatfoodies.fragments.ExploreFragmentDirections
import com.foodies.amatfoodies.fragments.HomeFragmentDirections
import com.foodies.amatfoodies.utils.ContextWrapper
import com.foodies.amatfoodies.utils.GlideApp
import com.foodies.amatfoodies.utils.ProjectUtils.withColor
import com.foodies.amatfoodies.utils.ProjectUtils.withColorStateList
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var context: Context
    private lateinit var destinationChangedListener: NavController.OnDestinationChangedListener
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var bottomNavView: BottomNavigationView

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

        bottomNavView = binding.bottomNavigationView

        binding.baseActivityToolbar.title = ""
        setSupportActionBar(binding.baseActivityToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        loadUserImageInNavigation(
            bottomNavView.menu.findItem(R.id.accountFragment),
            "https://firebasestorage.googleapis.com/v0/b/brendan-portfolio.appspot.com/o/" +
                    "portfolio%2Fuser%2Fme.jpg?alt=media&token=8f8fea71-a12b-46b9-9441-923f37c6ce3d"
        )

        binding.sideNavView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.sideMenuHome, R.id.sideMenuExplore, R.id.sideMenuAccount),
            binding.drawerLayout,
        )

        drawerToggle = ActionBarDrawerToggle(
            this@BaseActivity,
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
        NavigationUI.setupActionBarWithNavController(
            this@BaseActivity,
            navController,
            binding.drawerLayout
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)

        destinationChangedListener =
            NavController.OnDestinationChangedListener { controller, dest, _ ->
                when (dest.id) {
                    R.id.homeFragment -> {
                        setIconTint(R.id.homeFragment)
                    }
                    R.id.exploreFragment -> {
                        setIconTint(R.id.exploreFragment)
                    }
                    R.id.favoriteFragment -> {
                        setIconTint(R.id.favoriteFragment)
                    }
                    R.id.cartFragment -> {
                        setIconTint(R.id.cartFragment)
                    }
                    R.id.accountFragment -> {
                        setIconTint(R.id.accountFragment)
                        val item = bottomNavView.menu.findItem(R.id.accountFragment)
                        MenuItemCompat.setIconTintList(item, null)
                        MenuItemCompat.setIconTintMode(item, null)
                        bottomNavView.itemIconTintList = ColorStateList(
                            arrayOf(
                                intArrayOf(-android.R.attr.state_selected),
                                intArrayOf(android.R.attr.state_selected),
                            ), intArrayOf(
                                withColor(context, R.color.quantum_grey600),
                                withColor(context, R.color.transparent),
                            )
                        )
                    }
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

    private fun loadUserImageInNavigation(menuItem: MenuItem?, url: String? = null) {
        val value = if (!url.isNullOrBlank()) url else AllConstants.ANONYMOUS_USER_URL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            menuItem?.iconTintList = null
            menuItem?.iconTintMode = null
        }

        GlideApp.with(this@BaseActivity).asBitmap().load(value).optionalCircleCrop()
            .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.app_icon))
            .into(object : CustomTarget<Bitmap>(500, 500) {
                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap>?
                ) {
                    menuItem?.icon = BitmapDrawable(resources, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    override fun onBackPressed() {
        findNavController(R.id.base_activity_nav_host).navigateUp()
        super.onBackPressed()
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

    private fun setIconTint(@IdRes id: Int) {
        // Find Menu Item
        val item = bottomNavView.menu.findItem(id)
        if (item.itemId == bottomNavView.selectedItemId) {
            MenuItemCompat.setIconTintList(item, withColorStateList(context, R.color.primary100))
            MenuItemCompat.setIconTintMode(item, PorterDuff.Mode.SRC_ATOP)
        }

        // Set bottom nav icons tint
        bottomNavView.itemIconTintList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_selected),
            ), intArrayOf(
                withColor(context, R.color.quantum_grey600),
                withColor(context, R.color.primary100),
            )
        )
    }
}