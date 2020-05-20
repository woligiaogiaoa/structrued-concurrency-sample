package  com.jsn.android.search
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsn.android.R
import com.jsn.android.search.data.SearchLocalSource
import com.jsn.android.search.data.SearchRemoteSource
import com.jsn.android.search.data.SearchRepository
import com.jsn.android.search.data.SearchSource
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    val searchAdapter = SearchAdapter()

    //注入我们的viewModelFactory，在Factory注入viewModel所需的数据层实现类，这里是SearchRepository
    val viewModelFactory by lazy {
        InjectorUtil.provideSearchViewModelFactory()
    }

    val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory)
            .get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        restoreStateFromViewModel() //activity旋转之后重建，恢复搜索框里面的文字内容
        et_keyWord.addTextChangedListener { text ->
            with(viewModel) {
                keyWord.value = text.toString() //每次输入框里面的内容变化，我们都会开启一个协程，是并发场景，用来练习协程，难度刚刚好
            }
        }
        observeState()
        //显示搜索结果的recyclerview，设置一下。
        rv_search_result.adapter = searchAdapter
        rv_search_result.layoutManager = LinearLayoutManager(this)
    }

    private fun observeState() {
        viewModel.searchResult.observe(this) { result ->
            progress_bar.visibility = (result is Result.Loading).toVisibility() //只有在loading的时候我们才能看见 progress bar
            when (result) {
                is Result.Loading -> { tv_empty_search_result.visibility = View.GONE } //loading的时候我们要隐藏这个text为"无相关内容"的 TextView
                is Result.Error -> { showMessage(result.toString()) } //出错了，直接toast提示
                is Result.Success -> {
                    searchAdapter.submitList(result.data) //把搜索结果给 recyclerview 展示
                    tv_empty_search_result.visibility = (result.data.size == 0).toVisibility() //如果返回的数据是空的列表，提示用户无相关内容
                }
            }
        }
    }

    private fun restoreStateFromViewModel() {
        viewModel.keyWord.value?.also { keyWord ->
            et_keyWord.setText(keyWord)
        }
    }
}



/*-----------------------utilities-------------------------------*/

object InjectorUtil{
    fun provideSearchLocalSource():SearchSource=SearchLocalSource.instance
    fun provideSearchRemoteSource():SearchSource=SearchRemoteSource.instance
    fun provideSearchViewModelFactory(): SearchViewModelFactory = //给factory注入数据层接口实现类
        SearchViewModelFactory(SearchRepository(provideSearchRemoteSource(), provideSearchLocalSource()))
}


fun Boolean.toVisibility()=if(this) View.VISIBLE else View.GONE

fun Context.showMessage(data:String)= Toast.makeText(this,data, Toast.LENGTH_SHORT).show()

fun debug(s :String)= Log.e(TAG,s)

const val TAG="吐了"



