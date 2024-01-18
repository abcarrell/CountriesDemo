package com.example.countriesdemo

fun interface Interactor<T> : suspend () -> T
