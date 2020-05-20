package com.jsn.android.search.data

import java.lang.RuntimeException

class SearchLocalSource private constructor(): SearchSource{

    override suspend fun search(keyWord: String): List<String> {
        //这里直接返回空列表，永远不用本地数据。
        return if(isDirty()) emptyList() else throw RuntimeException("not implemented yet")
    }

    fun isDirty()=true //todo: 搞个数据库

    companion object{
        val instance by lazy {
            SearchLocalSource()
        }
    }

}