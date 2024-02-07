package com.tc.countries.entities

data class GroupItem<T>(
    val data: T,
    val itemType: Int
) {
    companion object {
        const val VIEWTYPE_HEADER = 0
        const val VIEWTYPE_ITEM = 1
    }
}

typealias GroupListing = List<GroupItem<*>>

fun <S, T> Map<S, List<T>>.groupListing(): GroupListing =
    map { entry ->
        listOf(GroupItem(entry.key, GroupItem.VIEWTYPE_HEADER)) +
                entry.value.map { GroupItem(it, GroupItem.VIEWTYPE_ITEM) }
    }.flatten()
