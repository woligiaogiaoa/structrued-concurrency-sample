package com.jsn.android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async


class  Starter(val applicationScope:CoroutineScope ) {

    val job = SupervisorJob()

    val customCoroutineContext = job + Dispatchers.Main.immediate

    val starterScope = CoroutineScope(customCoroutineContext)

    val task = starterScope.async {

    }

}