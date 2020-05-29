package com.jsn.android.mvvm_via_flow.data

import com.jsn.android.SearchRepository
import com.jsn.android.mvvm_via_flow.di.SearchBarActivityScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import com.jsn.android.search.Result
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi

@SearchBarActivityScope
class ChannelSearchRepository @Inject constructor() : SearchRepository { //测试用的repo

    val channel = ConflatedBroadcastChannel<String>()

    override fun getSearchResult(): Flow<Result<List<String>>> =
        channel.asFlow()
            .onStart {  }
            .map { key ->
                Result.Success(
                    if(key.isEmpty()) emptyList()
                    else channelData.filter { it.contains(key) }
                )
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



val channelData= mutableListOf<String>().apply {
    //测试数据
    add("123我爱你啊啊嗷嗷 ");add("17岁");add("123木头人")
    add("11111");add("1234");add("1990");add("1111");add("111");add("119")
    add("1121"); add("112");add("1111")
    add("111 Summer Classics");add("111111");add("111 (Centoundici)");add("11:11 (Amended)");
    add("11111101");add("11112")
    add("1111111");add("11111101");add("11111 (feat. Jake Candieux, Dan Monic..)");add("11111111")
}