package com.tc.countries.data

import retrofit2.Response
import java.io.IOException

fun interface DataMapper<S, T> : (S) -> T

fun interface ResponseDataMapper<T> : DataMapper<Response<T>, Result<T>>

fun <T> responseDataMapper() = ResponseDataMapper<T> { response ->
    runCatching {
        if (response.isSuccessful) checkNotNull(response.body()) { "Response is null" }
        else throw with(response) {
            IOException("API Error ${code()}: ${errorBody()?.string() ?: message()}")
        }
    }
}
