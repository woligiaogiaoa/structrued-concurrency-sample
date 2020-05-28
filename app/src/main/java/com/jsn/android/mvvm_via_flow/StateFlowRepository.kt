package com.jsn.android.mvvm_via_flow

import com.jsn.android.SearchRepository
import com.jsn.android.mvvm_via_flow.di.SearchBarActivityScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.jsn.android.search.Result
import kotlinx.coroutines.flow.*
import java.lang.Exception

@SearchBarActivityScope
class StateFlowRepository @Inject constructor() : SearchRepository {

    var keyWordFlow = MutableStateFlow<String>("")

    val resultFlow: Flow<Result<List<String>>> = keyWordFlow
        .map { keyWord ->
            delay(200)
            var filtered = repositoryData.filter {
                it.contains(keyWord, true)
            }
            if (keyWord.isEmpty()) {
                filtered = emptyList()
            }
            Result.Success(filtered) as Result<List<String>>
        }
        .flowOn(Dispatchers.Default)
        .catch { e ->
            emit(Result.Error(Exception(e)))
        }

    override fun getSearchResult(): Flow<Result<List<String>>> {
        return resultFlow
    }

    override fun onViewModelClear() {

    }

    override fun receiveSearchWords(keyWord: String) {
        keyWordFlow.value = keyWord
    }

    override fun keyWordOrNull(): String? {
        return keyWordFlow.value
    }

}