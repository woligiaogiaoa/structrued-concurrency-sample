package com.jsn.android.search.data

import kotlinx.coroutines.delay

class SearchRemoteSource private constructor() :SearchSource{  //这是一个假后端实现类

    override suspend fun search(keyWord: String): List<String> {

        delay(200)  //假装很耗时，模拟网络请求，这里200ms还是比较真实的

        if(keyWord.isEmpty()){ return emptyList()}

        return fakeRemoteData.filter { candidate ->
            candidate.contains(keyWord,true)
        }
    }

    /*伪造后端数据*/
    val fakeRemoteData= mutableListOf<String>().apply {

        //测试数据
        add("123我爱你");add("17岁");add("123木头人")
        add("11111");add("1234");add("1990");add("1111");add("111");add("119")
        add("1121"); add("112");add("1111")
        add("111 Summer Classics");add("111111");add("111 (Centoundici)");add("11:11 (Amended)");add("11111101");add("11112")
        add("1111111");add("11111101");add("11111 (feat. Jake Candieux, Dan Monic..)");add("11111111")

    }

    companion object{
        val instance by lazy {
            SearchRemoteSource()
        }
    }

}