package com.jsn.android.mvvm_via_flow.di

import android.content.Context
import com.jsn.android.mvvm_via_flow.SearchBarActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ActivityScope
@Subcomponent(modules = [RepositoryModule::class])
interface SearchBarComponent {

    fun inject(activity: SearchBarActivity)

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) :SearchBarComponent
    }
}