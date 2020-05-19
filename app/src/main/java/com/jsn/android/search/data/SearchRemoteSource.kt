package com.jsn.android.ffmpegplayground.search.data

import kotlinx.coroutines.delay

class SearchRemoteSource private constructor() :SearchSource{


    val fakeRemoteData= mutableListOf<String>().apply {
        add("香蕉")
        add("苹果")
        add("水泥")
        add("辣条")
        add("薯片")
        add("小龙虾")
        add("可乐")
        add("雪碧")
        add("爱了爱了")
        add("吐了，呕")
        repeat(10){
           add("${it}${it}")
        }
    }

    override suspend fun search(keyWord: String): List<String> {
        delay(500) //假装很耗时，模拟网络请求

        if(keyWord.isEmpty()){ return emptyList()}

        return fakeRemoteData.filter { candidate ->
            candidate.contains(keyWord,true)  //这里有一丢丢耗时
        }
    }

    /*suspend fun addToDataBase(keyWord: String){
        delay(1000) //pretend that the operation is very heavy
        fakeRemoteData.add(keyWord)
    }*/
    companion object{
        val instance by lazy {
            SearchRemoteSource()
        }
    }

}