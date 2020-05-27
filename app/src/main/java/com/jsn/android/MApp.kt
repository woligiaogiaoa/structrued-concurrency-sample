package com.jsn.android

import android.app.Application
import com.jsn.android.mvvm_via_flow.di.AppComponent
import com.jsn.android.mvvm_via_flow.di.DaggerAppComponent
import java.lang.Appendable
import java.lang.RuntimeException

class MApp :Application(){

    companion object{
        lateinit var application: Application
    }

    val appComponent by lazy {
        DaggerAppComponent.factory().create(application)
    }

    override fun onCreate() {
        super.onCreate()
        application=this
    }
}