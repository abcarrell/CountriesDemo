package com.abcarrell.countriesdemo.entities

data class GroupItem<T>(
    val data: T,
    val itemType: Int
)

typealias GroupListing = List<GroupItem<*>>

fun <S, T> Map<S, List<T>>.groupListing(): GroupListing =
    map { entry ->
        listOf(GroupItem(entry.key, 0)) + entry.value.map { GroupItem(it, 1) }
    }.flatten()
