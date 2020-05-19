package com.jsn.android.ffmpegplayground.search.data

import android.util.LruCache
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean

class SearchRepository(val searchRemoteSource: SearchRemoteSource,
                       val searchLocalSource: SearchLocalSource )
    :SearchSource {

    @Volatile var searchResultCache=LruCache<String,List<String>?>(20)

    val pending =AtomicBoolean(true)

    override suspend fun search(keyWord:String): List<String> {

        if(searchResultCache.atomicallyGet(keyWord,pending)!=null)
            return searchResultCache[keyWord]!!

        val local = searchLocalSource.search(keyWord)

        if(!local.isEmpty())
            return local.also { searchResultCache.atomicallyPut(keyWord,it,pending)  }

        return searchRemoteSource.search(keyWord).also { searchResultCache.atomicallyPut(keyWord,it,pending) }
    }
}

suspend fun <K,V> LruCache<K,V>.atomicallyPut(key:K, value:V, pending:AtomicBoolean ){
    while (true){
        if(pending.compareAndSet(true,false)){
            put(key,value)
            break;
        }
        delay(100)
    }
    pending.set(true)
}

suspend fun <K,V> LruCache<K,V?>.atomicallyGet(key:K, pending:AtomicBoolean ): V? {
    while (true){
        if(pending.compareAndSet(true,false)){
            val get = get(key)
            pending.set(true)
            return get
        }
        delay(100)
    }
}
