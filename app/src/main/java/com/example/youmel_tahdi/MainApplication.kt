package com.example.youmel_tahdi
import android.app.Application
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = "38adb7c7-e660-453a-957d-5b2dbbb3aa2b"

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}