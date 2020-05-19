package com.jsn.android.search.data

interface SearchSource{
    suspend fun search(keyWord:String): List<String>
}