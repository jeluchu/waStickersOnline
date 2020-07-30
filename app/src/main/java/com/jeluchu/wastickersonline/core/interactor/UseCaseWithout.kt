package com.jeluchu.wastickersonline.core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCaseWithout<Params>{
    abstract suspend fun run(params: Params)

    operator fun invoke(params: Params) {
        GlobalScope.launch(Dispatchers.Main) {
            val job = async(Dispatchers.IO) {run(params)}
            job.await()
        }
    }
}
