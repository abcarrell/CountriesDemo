package com.acarrell.countriesdemo

import android.app.Application

class CountriesDemo : Application() {

    companion object {
        const val APP_TAG = "CountriesDemo_"

        fun <T : Any> logTag(obj: T): String {
            return "${APP_TAG}${obj::class.java.simpleName}".take(35)
        }
    }
}