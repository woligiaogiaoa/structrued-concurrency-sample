package com.jsn.android.mvvm_via_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class SearchBarViewModel :ViewModel() {

    //自动处理 backpressure ，真是香死我惹。
    /*
    * 比如我们疯狂的往channel 里面 加关键字 ，可是我们 处理它 没那么快啊，毕竟我们是耗时操作，
    * 由于 是ConflatedBroadcastChannel（channel的一种，缓存大小为1），那么会取最近传过来的那么值。
    * 对于我们这个搜索框，这几乎是一种完美的解决方案。
    * */

    val channel =ConflatedBroadcastChannel<String>() //channel 的缓存大小为 1

    @FlowPreview
    val searchResultFlow :Flow<List<String>> = channel.asFlow()
        .map { keyWord ->
            //根据关键字获取搜索结果
            repositoty.filter { candidata ->
                candidata.contains(keyWord)
            }
        }
        .filter {
            delay(100) // 假装 很 耗时
            !it.isEmpty()
        }
        .flowOn(Dispatchers.IO) //这行代码 之前的代码 都在 IO线程中执行

    override fun onCleared() {
        super.onCleared()
        channel.close()
    }

    val repositoty= mutableListOf<String>().apply {
        //测试数据
        add("123我爱你");add("17岁");add("123木头人")
        add("11111");add("1234");add("1990");add("1111");add("111");add("119")
        add("1121"); add("112");add("1111")
        add("111 Summer Classics");add("111111");add("111 (Centoundici)");add("11:11 (Amended)");add("11111101");add("11112")
        add("1111111");add("11111101");add("11111 (feat. Jake Candieux, Dan Monic..)");add("11111111")
    }

}



@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class SearchBarViewModelFactory:ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(p0: Class<T>): T {
        return SearchBarViewModel() as T
    }
}