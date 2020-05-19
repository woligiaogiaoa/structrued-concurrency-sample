package com.jsn.android.ffmpegplayground.search

import androidx.lifecycle.*
import com.jsn.android.ffmpegplayground.Result
import com.jsn.android.ffmpegplayground.search.data.SearchRepository
import com.jsn.android.ffmpegplayground.search.data.SearchSource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class SearchViewModel(val repository: SearchSource):ViewModel(){

    val keyWord=MutableLiveData<String>()

    val searchResult: LiveData<Result<List<String>>> = keyWord.switchMap { keyWord ->
        liveData(Dispatchers.Default) {        //默认是主线程，如果代码可能存在阻塞风险，可以随时切换线程
            emit(Result.Loading)               //使用 liveData这个builder。不用管是在哪个线程 直接emit。
            emit(try {
                Result.Success(repository.search(keyWord))
            }catch (e:Exception){
                Result.Error(e)
            })
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(val repository: SearchSource):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(viewModel: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}



