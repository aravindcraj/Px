package com.aravindcraj.px

import android.app.Application
import com.aravindcraj.px.di.appModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Px : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Px)
            modules(appModule)
        }

        Stetho.initializeWithDefaults(this)
    }
}