package com.jsn.android.mvvm_via_flow

import com.jsn.android.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import com.jsn.android.search.Result

@FlowPreview
@ExperimentalCoroutinesApi
class ChannelSearchRepository : SearchRepository { //测试用的repo

    val channel = ConflatedBroadcastChannel<String>()

    override fun getSearchResult(): Flow<Result<List<String>>> =
        channel.asFlow()
            .onStart {  }
            .map { key ->
                Result.Success(repositoryData.filter {
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