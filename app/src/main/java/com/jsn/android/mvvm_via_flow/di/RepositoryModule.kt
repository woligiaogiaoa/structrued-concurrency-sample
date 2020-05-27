package com.jsn.android.mvvm_via_flow.di

import com.jsn.android.SearchRepository
import com.jsn.android.mvvm_via_flow.StateFlowRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideSearchBarRepository(repository:StateFlowRepository): SearchRepository
}