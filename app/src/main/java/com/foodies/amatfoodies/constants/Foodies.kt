package com.foodies.amatfoodies.constants

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp

class Foodies : Application() {
    override fun onCreate() {
        super.onCreate()

        if (instance == null) { //Check for the first time
            synchronized(Foodies::class.java) {   //Check for the second time.
                //if there is no instance available... create new one
                if (instance == null) instance = this
            }
        }

        try {
            FirebaseApp.initializeApp(this)
            Fresco.initialize(this)
        } catch (e: Exception) {
        }
    }

    companion object {
        var instance: Foodies? = null
    }
}