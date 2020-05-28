package com.jsn.android.mvvm_via_flow.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubomponents::class])
interface AppComponent {

    fun searchBarComponent():SearchBarActivityComponent.Factory

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application) :AppComponent
    }
}