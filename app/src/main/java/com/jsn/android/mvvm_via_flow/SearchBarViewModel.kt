package com.jsn.android.mvvm_via_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.jsn.android.search.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class SearchBarViewModel(val repository :SearchRepository) :ViewModel() {

    //flow 的逻辑下沉到 repository

    val searchResultFlowAsLiveData
            =repository.getSearchResult().asLiveData()

    val keyWord
    get() = repository.getValidKeyWordOrNull()


    fun offer(keyWord:String) =repository.receiveSearchWords(keyWord)

    override fun onCleared() {
        super.onCleared()
        repository.onViewModelClear()
    }


}

interface SearchRepository{

    fun getSearchResult() :Flow<Result<List<String>>>

    fun onViewModelClear()

    fun receiveSearchWords(keyWord: String)

    fun getValidKeyWordOrNull():String?
}

@FlowPreview
@ExperimentalCoroutinesApi
class TestSearchRepository :SearchRepository{

    val channel =ConflatedBroadcastChannel<String>()

    override fun getSearchResult(): Flow<Result<List<String>>> =
        channel.asFlow()
            .onStart {  }
            .map { key ->
                Result.Success(repositoty.filter {
                   it.contains(key)
                })
                as Result<List<String>>
            }
            .flowOn(Dispatchers.Default)
            .catch { e ->
                Result.Error(java.lang.Exception(e.message))
            }

    override fun onViewModelClear() {
        onClear()
    }

    override fun receiveSearchWords(keyWord: String) {
        channel.offer(keyWord)
    }

    override fun getValidKeyWordOrNull(): String? {
        return  channel.valueOrNull
    }

    fun onClear()=channel.close()


    val repositoty= mutableListOf<String>().apply {
        //测试数据
        add("123我爱你");add("17岁");add("123木头人")
        add("11111");add("1234");add("1990");add("1111");add("111");add("119")
        add("1121"); add("112");add("1111")
        add("111 Summer Classics");add("111111");add("111 (Centoundici)");add("11:11 (Amended)");
        add("11111101");add("11112")
        add("1111111");add("11111101");add("11111 (feat. Jake Candieux, Dan Monic..)");add("11111111")
    }

}



@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class SearchBarViewModelFactory(val repository: SearchRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(p0: Class<T>): T {
        return SearchBarViewModel(repository) as T
    }
}