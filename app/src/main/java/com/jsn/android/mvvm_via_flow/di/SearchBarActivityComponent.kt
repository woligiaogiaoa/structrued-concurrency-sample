package com.jsn.android.mvvm_via_flow.di

import android.content.Context
import com.jsn.android.mvvm_via_flow.SearchBarActivity
import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@SearchBarActivityScope
@Subcomponent(modules = [RepositoryModule::class])
interface SearchBarActivityComponent {

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun inject(activity: SearchBarActivity)

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) :SearchBarActivityComponent
    }
}