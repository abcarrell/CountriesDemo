package com.tc.countries.data

fun interface DataMapper<S, T> : (S) -> T
