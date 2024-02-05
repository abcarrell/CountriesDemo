package com.abcarrell.countriesdemo.data

import retrofit2.Response
import java.io.IOException

interface DataMapper<T, R> {
    fun map(value: T): R
    fun map(action: () -> T): R = map(action())
}

class ResponseDataMapper<T> : DataMapper<Response<T>, Result<T>> {
    override fun map(value: Response<T>): Result<T> = runCatching {
        if (value.isSuccessful) value.body() ?: throw RuntimeException("Response is null")
        else throw IOException("API Error: code ${value.code()} [${value.errorBody()?.string() ?: value.message()}]")
    }
}

fun <T> Response<T>.mapToResult(): Result<T> =
    ResponseDataMapper<T>().map(this)
