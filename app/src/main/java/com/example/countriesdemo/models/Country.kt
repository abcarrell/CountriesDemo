package com.example.countriesdemo.models

data class Country(
    val capital: String,
    val code: String,
    val currency: Currency,
    val flag: String,
    val language: Language,
    val name: String,
    val region: String
) {
    data class Currency(
        val code: String,
        val name: String,
        val symbol: String
    )

    data class Language(
        val code: String,
        val name: String
    )
}

typealias Countries = List<Country>
