package com.jsn.android.search

import androidx.lifecycle.*
import com.jsn.android.search.data.SearchRepository
import com.jsn.android.search.data.SearchSource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class SearchViewModel(val repository: SearchSource):ViewModel(){

    val keyWord=MutableLiveData<String>()

    val searchResult: LiveData<Result<List<String>>> = keyWord.switchMap { keyWord ->
        liveData(Dispatchers.Default) {        //默认是主线程，如果代码存在阻塞风险，可以随时切换线程
            emit(Result.Loading)               //使用 liveData这个builder。不用管是在哪个线程 直接emit。
            emit(try {
                Result.Success(repository.search(keyWord))
            }catch (e:Exception){
                Result.Error(e)
            })
        }
    }
}





