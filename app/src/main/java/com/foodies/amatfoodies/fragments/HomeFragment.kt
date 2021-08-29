package com.foodies.amatfoodies.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.activities.BaseActivity
import com.foodies.amatfoodies.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment(), FragmentManager.OnBackStackChangedListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mContext: Context
    private lateinit var activity: BaseActivity
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mContext = requireContext()
        activity = (requireActivity() as BaseActivity)

        val toolbar = binding.homeFragmentToolbar
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.title = ""
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerLayout = binding.drawerLayout
        navController = activity.findNavController(R.id.baseActivityNavHost)
        binding.sideNavigationView.setupWithNavController(navController)
        binding.sideNavigationView.setNavigationItemSelectedListener(this)

        drawerToggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.sideMenuHome, R.id.sideMenuExplore, R.id.sideMenuAccount),
            binding.drawerLayout,
        )

        activity.setupActionBarWithNavController(navController, drawerLayout)

        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackStackChanged() {
        val canGoBack: Boolean = activity.supportFragmentManager.backStackEntryCount > 0
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(canGoBack)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sideMenuHome -> {
                navController.navigate(
                    HomeFragmentDirections.actionSideMenuHomeToCartFragment()
                )
            }
            R.id.sideMenuExplore -> {
                navController.navigate(
                    ExploreFragmentDirections.actionSideMenuExploreToExploreFragment()
                )
            }
            R.id.sideMenuAccount -> {
                navController.navigate(
                    AccountFragmentDirections.actionSideMenuAccountToAccountFragment()
                )
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }

    private fun show(fragment: Fragment) {
        val fragmentManager = activity.supportFragmentManager

        fragmentManager
            .beginTransaction()
            .replace(R.id.home_fragment_content_view, fragment)
            .commit()

        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

//    fun onSupportNavigateUp(): Boolean {
//        activity.supportFragmentManager.popBackStack()
//        return true
//    }
}