package com.example.countriesdemo

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}

data class ViewHolderData(val item: Any, val viewType: Int)
