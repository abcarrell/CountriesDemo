package com.abcarrell.countriesdemo

// See Denis Brandi's article on using SAM interface for use case interactors:
// https://betterprogramming.pub/how-to-avoid-use-cases-boilerplate-in-android-d0c9aa27ef27
fun interface Interactor<T> : suspend () -> T
