package com.jeluchu.wastickersonline.core.exception

sealed class Failure {
    class NetworkConnection : Failure()
    class ServerError : Failure()
    data class CustomError(val errorCode: Int, val errorMessage: String?) : Failure()
}