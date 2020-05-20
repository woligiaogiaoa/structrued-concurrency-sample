package com.jsn.android.search.data

import java.lang.RuntimeException

class SearchLocalSource private constructor(): SearchSource{

    override suspend fun search(keyWord: String): List<String> {
        //因为并没有实现本地数据库
        //这里直接返回空的搜索结果.本地数据永远视为脏数据
        return if(isDirty()) emptyList() else throw RuntimeException("not implemented yet")
    }

    fun isDirty()=true //todo: 搞个数据库

    companion object{
        val instance by lazy {
            SearchLocalSource()
        }
    }

}