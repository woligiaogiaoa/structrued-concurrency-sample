package com.jsn.android.mvvm_via_flow

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsn.android.R
import com.jsn.android.search.Result
import com.jsn.android.search.SearchActivity
import com.jsn.android.search.SearchAdapter
import com.jsn.android.search.showMessage
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect


@ExperimentalCoroutinesApi
@FlowPreview
class SearchBarActivity  :AppCompatActivity(){

    lateinit var adapter: SearchAdapter

    val viewModel by viewModels<SearchBarViewModel>(
        SearchBarViewModelFactory() )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        rv_search_result.adapter=SearchAdapter().also { adapter=it }
        rv_search_result.layoutManager=LinearLayoutManager(this)
        et_keyWord.doAfterTextChanged { keyWord->
        with(viewModel){
               channel.offer(keyWord.toString())
           }
        }

        lifecycleScope.launchWhenStarted {
           viewModel.searchResultFlow.collect { searchResult ->
               when(searchResult){
                   is Result.Loading -> {}
                   is Result.Error-> { showMessage(searchResult.toString())}
                   is Result.Success -> {adapter.submitList(searchResult.data) }
               }
           }
        }
    }
}