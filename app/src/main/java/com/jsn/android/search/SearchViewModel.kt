package com.jsn.android.search

import androidx.lifecycle.*
import com.jsn.android.search.data.SearchRepository
import com.jsn.android.search.data.SearchSource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class SearchViewModel(val repository: SearchSource):ViewModel(){

    val keyWord=MutableLiveData<String>()

    val searchResult: LiveData<Result<List<String>>> = keyWord.switchMap { keyWord ->
        liveData(Dispatchers.Default) {
            emit(Result.Loading)
            emit(try {
                Result.Success(repository.search(keyWord))
            }catch (e:Exception){
                Result.Error(e)
            })
        }
    }
}





