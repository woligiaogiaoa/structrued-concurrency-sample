package com.jsn.android.search

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/*sealed class Result<out T>{

    object Loading:Result<Nothing>()

    data class Error(val e:Exception):Result<Nothing>()

    data class Success<T>(val data:T) :Result<T>()
}*/

sealed class Result<out T>{
    override fun toString(): String {
        return when(this){
            Loading -> "losding"
            is Error -> "Error[exception=${e}]"
            is Success -> "Success[data=${data}]"
        }
    }
    object Loading:Result<Nothing>()

    data class Error(val e:Exception):Result<Nothing>()

    data class Success<T>(val data:T) :Result<T>()
}



/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data   ?: fallback
}

@MainThread
inline fun <T> LiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Observer<T> {
    val wrappedObserver = Observer<T> { t -> onChanged.invoke(t) }
    observe(owner, wrappedObserver)
    return wrappedObserver
}