package com.foodies.amatfoodies.constants

import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by foodies on 10/18/2019.
 */
object AllConstants {
    var width = 0

    @JvmField
    var height = 0

    @JvmField
    var verdana = "verdana.ttf"
    var arial = "arial.ttf"
    const val CALCULATION = "CalculationAndroid"
    const val TRACKING = "tracking"
    const val tag = "foodies_customer"

    @JvmField
    var folder_parcel = Environment.getExternalStorageDirectory().toString() + "/Foodies/"

    @JvmField
    var folder_parcel_DCIM = Environment.getExternalStorageDirectory().toString() + "/DCIM/Foodies/"

    @JvmField
    var df = SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.getDefault())

    @JvmField
    var df2 = SimpleDateFormat("dd-MM-yyyy HH:mmZZ", Locale.getDefault())
    const val permission_location = 790
    const val permission_camera_code = 786
    const val permission_write_data = 788
    const val permission_Read_data = 789
    const val permission_Recording_audio = 790
    const val Request_code_Location = 800
    const val IsToastShow = true
    const val ISShowAd = true
    const val max_zoom = 15

    @JvmField
    var Opened_Chat_id = ""
    const val ANONYMOUS_USER_URL = "https://firebasestorage.googleapis.com/v0/b/" +
            "brendan-portfolio.appspot.com/o/misc%2Funnamed.png" +
            "?alt=media&token=777f8472-69a0-40be-aaea-bf3e85c34afd"
}