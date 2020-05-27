package com.jsn.android.mvvm_via_flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsn.android.MApp
import com.jsn.android.R
import com.jsn.android.mvvm_via_flow.di.DaggerAppComponent
import com.jsn.android.search.Result
import com.jsn.android.search.SearchAdapter
import com.jsn.android.search.showMessage
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@ExperimentalCoroutinesApi
@FlowPreview
class SearchBarActivity : AppCompatActivity() {

    lateinit var adapter: SearchAdapter

    @Inject
    lateinit var factory: SearchBarViewModelFactory

    lateinit var  viewModel: SearchBarViewModel

    val searchBarActivityComponent by lazy {
        (application as MApp).appComponent.searchBarComponent().create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        searchBarActivityComponent.inject(this) //inject
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this,factory).get(SearchBarViewModel::class.java)
        setContentView(R.layout.activity_search)
        rv_search_result.adapter = SearchAdapter().also { adapter = it }
        rv_search_result.layoutManager = LinearLayoutManager(this)

        et_keyWord.doAfterTextChanged { text ->
            with(viewModel) {
                offer(keyWord = text.toString())
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.searchResultFlow.collect { searchResult ->
                when (searchResult) {
                    is Result.Loading -> { }
                    is Result.Error -> { showMessage(searchResult.toString()) }
                    is Result.Success -> { adapter.submitList(searchResult.data) }
                }
            }
        }

        viewModel.keyWord?.run {
            et_keyWord.setText(this)
        }
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
object SearchBarActivtityInjector {

    fun getSearchRepository() = StateFlowRepository() //注入测试用的repository

    fun provideSearchBarViewModelFactory() = SearchBarViewModelFactory(getSearchRepository())
}