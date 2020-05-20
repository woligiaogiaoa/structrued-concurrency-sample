package com.jsn.android.search.data

import android.util.LruCache
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max
import kotlin.math.min

class SearchRepository(val searchRemoteSource: SearchSource,
                       val searchLocalSource: SearchSource )
    :SearchSource {

    @Volatile var searchResultCache=LruCache<String,List<String>?>(20)

    val pending =AtomicBoolean(true)

    override suspend fun search(keyWord:String): List<String> {

        val getFromCache = searchResultCache.atomicallyGet(keyWord, pending)
        if(getFromCache!=null) return getFromCache  //如果从缓存中找到了，直接返回

        val localResult = searchLocalSource.search(keyWord)
        if(!localResult.isEmpty())    //如果从本地数据源（比如数据库）找到了有效数据，直接返回，并把数据存入 lruCache
            return localResult.also { searchResultCache.atomicallyPut(keyWord,it,pending)  }

        //从后端获取数据，并存入 lruchche, todo :实现一个本地数据库，存入数据库。
        return searchRemoteSource.search(keyWord).also { searchResultCache.atomicallyPut(keyWord,it,pending) }
    }
}

/*-------------------------LruCache扩展函数，多线程环境下，保证LruCache的put，get操作的原子性，从而实现线程安全
*                          自定义了线程 操作失败到下次重试的时间间隔。如果不需要自定义，可以直接使用Collections.synchronizedMap(LinkedHashMap())来实现
*                          一个线程安全的LruCache       */


suspend fun <K,V> LruCache<K,V>.atomicallyPut(key:K, value:V, pending:AtomicBoolean ){
    while (true){
        if(pending.compareAndSet(true,false)){
            put(key,value)
            break
        }
        delay(50)
    }
    pending.set(true)
}

suspend fun <K,V> LruCache<K,V?>.atomicallyGet(key:K, pending:AtomicBoolean ): V? {

    var retryInterval=50L
    while (true){
        if(pending.compareAndSet(true,false)){
            val get = get(key)
            pending.set(true)
            return get
        }
        retryInterval+=50
        delay(min(retryInterval,3_000L))
    }
}

/*//todo: refactor liveData to flow
   fun searchFlow(keyWord: String)= flow<List<String>> {
       val getFromCache = searchResultCache.atomicallyGet(keyWord, pending)
       if(getFromCache!=null) emit(getFromCache)

       val localResult = searchLocalSource.search(keyWord)
       if(!localResult.isEmpty())
           emit(localResult.also { searchResultCache.atomicallyPut(keyWord,it,pending)  } )

       emit( searchRemoteSource.search(keyWord).also { searchResultCache.atomicallyPut(keyWord,it,pending) } )
   }*/
