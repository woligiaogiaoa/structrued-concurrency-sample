package com.jsn.android.mvvm_via_flow

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jsn.android.SearchRepository
import com.jsn.android.mvvm_via_flow.di.ChannelRepo
import com.jsn.android.mvvm_via_flow.di.StateFlowRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class SearchBarViewModelFactory @Inject constructor(
    val application: Application,
    @ChannelRepo val repository: SearchRepository
) : ViewModelProvider.Factory {
    @FlowPreview
    override fun <T : ViewModel?> create(p0: Class<T>): T {
        return SearchBarViewModel(repository, application) as T
    }
}