package com.jeluchu.wastickersonline.core.extensions

import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.functional.Either
import retrofit2.Call

fun <T, R> request(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> Either.Right(transform((response.body() ?: default)))
            false -> Either.Left(Failure.CustomError(response.code(), response.message()))
        }
    } catch (exception: Throwable) {
        Either.Left(Failure.ServerError())
    }
}
