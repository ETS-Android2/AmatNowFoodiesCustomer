package com.foodies.amatfoodies.activitiesAndFragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.foodies.amatfoodies.BuildConfig
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.activities.BaseActivity
import com.foodies.amatfoodies.constants.AllConstants
import com.foodies.amatfoodies.constants.DarkModePrefManager
import com.foodies.amatfoodies.constants.Functions
import com.foodies.amatfoodies.constants.PreferenceClass
import com.foodies.amatfoodies.googleMapWork.MapsActivity
import com.foodies.amatfoodies.utils.ContextWrapper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import java.util.*

class SplashScreen : AppCompatActivity() {
    private lateinit var welcomeLocationTxt: TextView
    private lateinit var mainWelcomeScreenLayout: RelativeLayout
    private lateinit var mainSplashLayout: RelativeLayout
    private lateinit var welcomeSearchDiv: RelativeLayout
    private var getCurrentLocationAddress: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mLocationRequest: LocationRequest
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var welcomeShowRestaurantsBtn: Button
    private var locationCallback: LocationCallback? = null
    private lateinit var videoView: VideoView
    private var latitude = 0.0
    private var longitude = 0.0

    override fun attachBaseContext(newBase: Context) {
        val languageArray = newBase.resources.getStringArray(R.array.language_code)
        val languageCode = mutableListOf(*languageArray)
        sharedPreferences = newBase.getSharedPreferences(PreferenceClass.user, MODE_PRIVATE)
        val language = sharedPreferences.getString(PreferenceClass.selected_language, "")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && languageCode.contains(language)) {
            val newLocale = Locale(language)
            val context: Context = ContextWrapper.wrap(newBase, newLocale)
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (DarkModePrefManager(this).isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.splash)
            sharedPreferences = getSharedPreferences(PreferenceClass.user, MODE_PRIVATE)
            getCurrentLocationAddress =
                sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, "")
            /* if (!getCurrentLocationAddress.isEmpty()) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }*/if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                setLanguageLocal()
            }

            // Video View for Splash Screen
            videoView = findViewById<View>(R.id.videoView) as VideoView
            val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.logomotionsmall)
            videoView.setVideoURI(video)
            videoView.setOnCompletionListener { mp: MediaPlayer? ->
                val getUserType = sharedPreferences.getString(PreferenceClass.USER_TYPE, "")
                val getLoINSession = sharedPreferences.getBoolean(PreferenceClass.IS_LOGIN, false)
                if (!getLoINSession) {
                    //Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                    //Intent i = new Intent(SplashScreen.this, GetStartedActivity.class);
                    //startActivity(i);
                    //finish();
                    startNextActivity()
                } else {
                    if (getUserType.equals("user", ignoreCase = true)) {
                        startNextActivity()
                        /*Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                            // Intent i = new Intent(SplashScreen.this, BaseActivity.class);
                            startActivity(i);
                            finish();*/
                    }
                }
            }
            videoView.start()

            // End Video View for Splash Screen
            versionCode = BuildConfig.VERSION_NAME
            val androidId = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val editor2 = sharedPreferences.edit()
            editor2.putString(PreferenceClass.UDID, androidId).apply()
            createLocationRequest()
            startLocationUpdates()
            welcomeLocationTxt = findViewById(R.id.welcome_location_txt)
            mainWelcomeScreenLayout = findViewById(R.id.main_welcome_screen_layout)
            mainSplashLayout = findViewById(R.id.main_splash_layout)
            welcomeSearchDiv = findViewById(R.id.welcome_search_div)
            welcomeShowRestaurantsBtn = findViewById(R.id.welcome_show_restaurants_btn)
            welcomeShowRestaurantsBtn.setOnClickListener { /*startActivity(new Intent(SplashScreen.this, HomeScreen.class));
                    //startActivity(new Intent(SplashScreen.this, BaseActivity.class));
                    MapsActivity.SAVE_LOCATION = false;
                    finish();*/
                val nextScreen = Intent(this@SplashScreen, BaseActivity::class.java)
                nextScreen.putExtra("search", "any")
                startActivity(nextScreen)
            }
            welcomeSearchDiv.setOnClickListener {
                val i = Intent(this@SplashScreen, MapsActivity::class.java)
                startActivityForResult(i, PERMISSION_DATA_ACCESS_CODE)
            }
            if (getCurrentLocationAddress != null && getCurrentLocationAddress!!.isNotEmpty()) {
                mainWelcomeScreenLayout.visibility = View.GONE
                mainSplashLayout.visibility = View.VISIBLE

                /* new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        String getUserType = sharedPreferences.getString(PreferenceClass.USER_TYPE, "");
                        boolean getLoINSession = sharedPreferences.getBoolean(PreferenceClass.IS_LOGIN, false);

                        if (!getLoINSession) {
                            */
                /*Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                            // Intent i = new Intent(SplashScreen.this, BaseActivity.class);
                            startActivity(i);
                            finish();*/
                /*
                            Intent nextScreen = new Intent(SplashScreen.this, BaseActivity.class);
                            nextScreen.putExtra("search", "any");
                            startActivity(nextScreen);
                        } else {
                            if (getUserType.equalsIgnoreCase("user")) {
                                */
                /*Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                                // Intent i = new Intent(SplashScreen.this, BaseActivity.class);
                                startActivity(i);
                                finish();*/
                /*
                                Intent nextScreen = new Intent(SplashScreen.this, BaseActivity.class);
                                nextScreen.putExtra("search", "any");
                                startActivity(nextScreen);
                            }

                        }
                    }
                }, SPLASH_TIME_OUT);*/
            } else {
                checkLocationPermission()
            }
        } catch (e: Exception) {
        }
    }

    // Start Activity After Video
    private fun startNextActivity() {
        if (isFinishing) return
        val nextScreen = Intent(this@SplashScreen, BaseActivity::class.java)
        nextScreen.putExtra("search", "any")
        startActivity(nextScreen)
    }

    private fun setLanguageLocal() {
        val languageArray = resources.getStringArray(R.array.language_code)
        val languageCode = mutableListOf(*languageArray)
        val language = sharedPreferences.getString(PreferenceClass.selected_language, "")
        if (languageCode.contains(language)) {
            val myLocale = Locale(language)
            val res = resources
            val dm = res.displayMetrics
            val conf = Configuration()
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
            onConfigurationChanged(conf)
        }
    }

    private fun checkLocationPermission() {
        if (Functions.checkLocationStatus(this)) {
            createLocationRequest()
            startLocationUpdates()
        } else {
            startActivityForResult(
                Intent(this, Enable_location_A::class.java),
                AllConstants.Request_code_Location
            )
        }
    }

    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(
                    this, result,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )?.show()
            } else {
                finish()
            }
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        checkPlayServices()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_DATA_ACCESS_CODE) {
            if (resultCode == RESULT_OK) {
                latitude = data!!.getStringExtra("lat")!!.toDouble()
                longitude = data.getStringExtra("lng")!!.toDouble()
                val locationAddress: Address? = getAddress(latitude, longitude)
                if (locationAddress != null) {
                    var city = ""
                    if (locationAddress.locality != null && locationAddress.locality != "null") city =
                        "" + locationAddress.locality
                    var country = ""
                    if (locationAddress.countryName != null && locationAddress.countryName != "null") country =
                        "" + locationAddress.countryName
                    val editor = sharedPreferences.edit()
                    editor.putString(
                        PreferenceClass.CURRENT_LOCATION_LAT_LONG,
                        "$latitude,$longitude"
                    )
                    editor.putString(PreferenceClass.CURRENT_LOCATION_ADDRESS, "$city $country")
                    editor.putString(PreferenceClass.LATITUDE, latitude.toString())
                    editor.putString(PreferenceClass.LONGITUDE, longitude.toString())
                    editor.apply()
                    welcomeLocationTxt.text = getCurrentLocationAddress
                    welcomeLocationTxt.text =
                        String.format("%s %s", city, country, Locale.getDefault())
                }
            }
        } else if (requestCode == 3) {
            checkLocationPermission()
        } else if (requestCode == AllConstants.Request_code_Location) {
            Log.d(AllConstants.tag, "Request_code_Location")
            checkLocationPermission()
        }
    }

    fun getAddress(latitude: Double, longitude: Double): Address? {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission()
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest.fastestInterval = FASTEST_INTERVAL.toLong()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        val locationAddress: Address? = getAddress(latitude, longitude)
                        if (locationAddress != null) {
                            var city = ""
                            if (locationAddress.locality != null && locationAddress.locality != "null") city =
                                "" + locationAddress.locality
                            var country = ""
                            if (locationAddress.countryName != null && locationAddress.countryName != "null") country =
                                "" + locationAddress.countryName
                            if (!getCurrentLocationAddress!!.isEmpty()) {
                                welcomeLocationTxt.text = getCurrentLocationAddress
                            } else {
                                val editor = sharedPreferences.edit()
                                editor.putString(
                                    PreferenceClass.CURRENT_LOCATION_LAT_LONG,
                                    "$latitude,$longitude"
                                )
                                editor.putString(
                                    PreferenceClass.CURRENT_LOCATION_ADDRESS,
                                    "$city $country"
                                )
                                editor.putString(PreferenceClass.LATITUDE, latitude.toString())
                                editor.putString(PreferenceClass.LONGITUDE, longitude.toString())
                                editor.apply()
                                welcomeLocationTxt.text = getCurrentLocationAddress
                                welcomeLocationTxt.text =
                                    String.format("%s %s", city, country, Locale.getDefault())
                            }
                        }
                    } else {
                        welcomeLocationTxt.text =
                            String.format("Kalma Chowk, Lahore", Locale.getDefault())
                    }
                    stopLocationUpdates()
                }
            }
        }
        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest, locationCallback, Looper.myLooper()
        )
    }

    private fun stopLocationUpdates() {
        if (mFusedLocationClient != null && locationCallback != null)
            mFusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    public override fun onDestroy() {
        stopLocationUpdates()
        super.onDestroy()
    }

    companion object {
        @JvmField
        var versionCode: String? = null
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val PERMISSION_DATA_ACCESS_CODE = 2
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 1000
        private const val UPDATE_INTERVAL = 3000
        private const val FASTEST_INTERVAL = 3000
        private const val DISPLACEMENT = 0
        private const val SPLASH_TIME_OUT = 6000
    }
}