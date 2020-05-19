package com.jsn.android.ffmpegplayground

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jsn.android.ffmpegplayground.search.SearchViewModelFactory
import com.jsn.android.ffmpegplayground.search.data.SearchLocalSource
import com.jsn.android.ffmpegplayground.search.data.SearchRemoteSource
import com.jsn.android.ffmpegplayground.search.data.SearchRepository

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