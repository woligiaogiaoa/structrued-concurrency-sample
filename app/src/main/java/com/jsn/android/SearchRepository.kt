package com.jsn.android

import com.jsn.android.search.Result
import kotlinx.coroutines.flow.Flow

interface SearchRepository{

    fun getSearchResult() : Flow<Result<List<String>>>

    fun onViewModelClear()

    fun receiveSearchWords(keyWord: String)

    fun keyWordOrNull():String?
}