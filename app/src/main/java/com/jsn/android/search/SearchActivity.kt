package  com.jsn.android.search
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsn.android.R
import com.jsn.android.search.*
import com.jsn.android.search.SearchAdapter
import com.jsn.android.search.SearchViewModel
import com.jsn.android.search.data.SearchLocalSource
import com.jsn.android.search.data.SearchRemoteSource
import com.jsn.android.search.data.SearchRepository
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    val searchAdapter= SearchAdapter()

    val viewModelFactory by lazy{
        InjectorUtil.provideSearchViewModelFactory()
    }

    val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory)
            .get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        et_keyWord.addTextChangedListener {
            with(viewModel){
                keyWord.value=it.toString()
            }
        }
        observeState()
        rv_search_result.adapter=searchAdapter
        rv_search_result.layoutManager=LinearLayoutManager(this)
    }

    private fun observeState() {
        viewModel.searchResult.observe(this){ result ->
            progress_bar.visibility=(result is Result.Loading).toVisibility()
            when(result){
                is Result.Loading -> { tv_empty_search_result.visibility=View.GONE}
                is Result.Error -> { showMessage(result.toString()) }
                is Result.Success -> {
                    searchAdapter.submitList(result.data)
                    tv_empty_search_result.visibility=(result.data.size==0).toVisibility()
                }
            }
        }
    }
}

fun Boolean.toVisibility()=if(this) View.VISIBLE else View.GONE

fun Context.showMessage(data:String)= Toast.makeText(this,data, Toast.LENGTH_SHORT).show()

fun debug(s :String)= Log.e(TAG,s)

const val TAG="吐了"

object InjectorUtil{
    fun provideSearchLocalSource()=SearchLocalSource.instance
    fun provideSearchRemoteSource()=SearchRemoteSource.instance
    fun provideSearchViewModelFactory()=
        SearchViewModelFactory(SearchRepository(provideSearchRemoteSource(), provideSearchLocalSource()))
}


