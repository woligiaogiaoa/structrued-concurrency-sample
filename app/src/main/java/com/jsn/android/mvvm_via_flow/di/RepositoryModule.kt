package com.jsn.android.mvvm_via_flow.di

import com.jsn.android.SearchRepository
import com.jsn.android.mvvm_via_flow.ChannelSearchRepository
import com.jsn.android.mvvm_via_flow.StateFlowRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
class RepositoryModule {
    @StateFlowRepo
    @Provides
    fun provideStateFlowSearchBarRepository(): SearchRepository=StateFlowRepository()

    @ChannelRepo
    @Provides
    fun provideChannelSearchReposotory():SearchRepository=ChannelSearchRepository()
}


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class StateFlowRepo

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ChannelRepo
