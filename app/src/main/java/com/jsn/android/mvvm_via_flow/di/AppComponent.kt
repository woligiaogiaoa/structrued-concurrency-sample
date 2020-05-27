package com.jsn.android.mvvm_via_flow.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubomponents::class])
interface AppComponent {

    fun searchBarComponent():SearchBarComponent.Factory

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application) :AppComponent
    }
}