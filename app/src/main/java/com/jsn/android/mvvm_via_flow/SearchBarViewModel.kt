package com.jsn.android.mvvm_via_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.jsn.android.search.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.lang.Exception

@FlowPreview
@ExperimentalCoroutinesApi
class SearchBarViewModel(val repository :SearchRepository) :ViewModel() {

    //flow 的逻辑下沉到 repository

    val searchResultFlowAsLiveData
            =repository.getSearchResult().asLiveData()

    val keyWord
    get() = repository.keyWordOrNull()

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

    fun keyWordOrNull():String?
}

class StateFlowRepository():SearchRepository{

    var keyWordFlow= MutableStateFlow<String>("")

    val resultFlow=
        keyWordFlow.map { keyWord ->
            delay(200)
            var filtered = repositotyData.filter {
                it.contains(keyWord, true)
            }
            if(keyWord.isEmpty()){ filtered= emptyList() }
            Result.Success(filtered) as Result<List<String>>
        }
        .flowOn(Dispatchers.Default)

    override fun getSearchResult(): Flow<Result<List<String>>> {
        return resultFlow
    }

    override fun onViewModelClear() {

    }

    override fun receiveSearchWords(keyWord: String) {
        keyWordFlow.value=keyWord
    }

    override fun keyWordOrNull(): String? {
        return keyWordFlow.value
    }

}

@FlowPreview
@ExperimentalCoroutinesApi
class ChannelSearchRepository :SearchRepository{ //测试用的repo

    val channel =ConflatedBroadcastChannel<String>()

    override fun getSearchResult(): Flow<Result<List<String>>> =
        channel.asFlow()
            .onStart {  }
            .map { key ->
                Result.Success(repositotyData.filter {
                   it.contains(key)
                })
                as Result<List<String>>
            }
            .flowOn(Dispatchers.Default)
            .catch { e ->
                Result.Error(Exception(e))
            }

    override fun onViewModelClear() {
        onClear()
    }

    override fun receiveSearchWords(keyWord: String) {
        channel.offer(keyWord)
    }

    override fun keyWordOrNull(): String? {
        return  channel.valueOrNull
    }

    fun onClear()=channel.close()




}

val repositotyData= mutableListOf<String>().apply {
    //测试数据
    add("123我爱你");add("17岁");add("123木头人")
    add("11111");add("1234");add("1990");add("1111");add("111");add("119")
    add("1121"); add("112");add("1111")
    add("111 Summer Classics");add("111111");add("111 (Centoundici)");add("11:11 (Amended)");
    add("11111101");add("11112")
    add("1111111");add("11111101");add("11111 (feat. Jake Candieux, Dan Monic..)");add("11111111")
}



@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class SearchBarViewModelFactory(val repository: SearchRepository):ViewModelProvider.Factory{
    @FlowPreview
    override fun <T : ViewModel?> create(p0: Class<T>): T {
        return SearchBarViewModel(repository) as T
    }
}