package com.foodies.amatfoodies.constants

import android.content.Context
import android.content.SharedPreferences
import com.foodies.amatfoodies.constants.PreferenceClass

/**
 * Created by foodies on 10/18/2019.
 */
object PreferenceClass {
    @JvmField
    var sharedPreferences: SharedPreferences? = null

    @JvmStatic
    fun getSharedPreference(context: Context): SharedPreferences? {
        return if (sharedPreferences != null) sharedPreferences else context.getSharedPreferences(
            user,
            Context.MODE_PRIVATE
        ).also { sharedPreferences = it }
    }

    const val user = "User"
    const val pre_email = "pre_email"
    const val pre_pass = "pre_pass"
    const val pre_user_id = "pre_user_id"
    const val pre_contact = "pre_contact"
    const val pre_first = "pre_first"
    const val pre_last = "pre_last"
    const val IS_LOGIN = "isLogIN"
    const val CURRENT_LOCATION_LAT_LONG = "currentLocationLATLONG"
    const val CURRENT_LOCATION_ADDRESS = "cuurentLocationAddress"
    const val LATITUDE = "lat"
    const val LONGITUDE = "long"
    const val USER_TYPE = "userType"
    const val UDID = "udid"
    const val device_token = "device_token"
    const val selected_language = "selected_language"
    const val HAS_SAVED_LOCATION_ONCE = "false"
    const val CREDIT_CARD_ARRAY = "creditCardArray"
    const val CREDIT_CARD_BRAND = "creditCardBrand"
    const val COUNTRY_NAME = "countryName"
    const val u_currentLat = "u_currentLat"
    const val u_currentlng = "u_currentlng"
    const val STREET = "street"
    const val CITY = "city"
    const val STATE = "state"
    const val INSTRUCTIONS = "instructions"
    const val APARTMENT = "apartment"
    const val ADDRESS_ID = "addressID"
    const val ADDRESS_DELIVERY_FEE = "addressDeliveryFee"
    const val PAYMENT_ID = "paymentID"
    const val ORDER_HEADER = "orderHeader"
    const val ORDER_ID = "orderID"
    const val ORDER_INS = "orderInst"
    const val ORDER_QUANTITY = "orderQuantity"
    const val DEALS_QUANTITY = "dealsQuantity"
    const val CART_COUNT = "cartCount"
    const val RESTAURANT_ID_NOTIFY = "restarantID_Notify"
    const val RESTAURANT_NAME_NOTIFY = "restaurantName_Notify"
    const val ORDER_ID_NOTIFY = "orderID_Notify"
    const val RIDER_NAME_NOTIFY = "riderID_Notify"
    const val RIDER_USER_ID_NOTIFY = "riderUserID_Notify"
    const val REVIEW_TYPE = "reviewType"
    const val REVIEW_IMG_PIC = "reviewImgPic"
    const val FIRSTIMER = "firstTime"
    const val SEARCH = "search_para"
}