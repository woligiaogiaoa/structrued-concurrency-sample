package com.jsn.android.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jsn.android.search.data.SearchSource

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(val repository: SearchSource) //在构造函数中注入 数据层接口 的 实现类
    : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(viewModel: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}