package com.jsn.android.ffmpegplayground.search.data

interface SearchSource{
    suspend fun search(keyWord:String): List<String>
}