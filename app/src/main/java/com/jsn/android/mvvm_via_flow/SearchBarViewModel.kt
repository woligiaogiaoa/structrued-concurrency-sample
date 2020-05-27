package com.jsn.android.mvvm_via_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.jsn.android.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

import com.jsn.android.search.Result

@FlowPreview
@ExperimentalCoroutinesApi
class SearchBarViewModel(val repository :SearchRepository) :ViewModel() {

    //flow 的逻辑下沉到 repository

    val searchResultFlow
            =repository.getSearchResult()

    val keyWord
    get() = repository.keyWordOrNull()

    fun offer(keyWord:String) =repository.receiveSearchWords(keyWord)

    override fun onCleared() {
        super.onCleared()
        repository.onViewModelClear()
    }

}







val repositoryData= mutableListOf<String>().apply {
    //测试数据
    add("123我爱你");add("17岁");add("123木头人")
    add("11111");add("1234");add("1990");add("1111");add("111");add("119")
    add("1121"); add("112");add("1111")
    add("111 Summer Classics");add("111111");add("111 (Centoundici)");add("11:11 (Amended)");
    add("11111101");add("11112")
    add("1111111");add("11111101");add("11111 (feat. Jake Candieux, Dan Monic..)");add("11111111")
}



