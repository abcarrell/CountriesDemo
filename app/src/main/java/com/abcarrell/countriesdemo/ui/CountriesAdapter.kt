package com.abcarrell.countriesdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcarrell.countriesdemo.BaseViewHolder
import com.abcarrell.countriesdemo.R
import com.abcarrell.countriesdemo.databinding.CountryLayoutBinding
import com.abcarrell.countriesdemo.databinding.HeaderLayoutBinding
import com.abcarrell.countriesdemo.entities.Country
import com.abcarrell.countriesdemo.entities.GroupItem.Companion.VIEWTYPE_HEADER
import com.abcarrell.countriesdemo.entities.GroupItem.Companion.VIEWTYPE_ITEM
import com.abcarrell.countriesdemo.entities.GroupListing

class CountriesAdapter : RecyclerView.Adapter<BaseViewHolder<*>>() {
    var data: GroupListing = listOf()
        set(value) {
            notifyItemRangeRemoved(0, data.size)
            field = value
            notifyItemRangeInserted(0, data.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return LayoutInflater.from(parent.context).let { layoutInflater ->
            when (viewType) {
                VIEWTYPE_HEADER -> HeaderLayoutBinding.inflate(layoutInflater, parent, false)
                    .run { HeaderViewHolder(this) }

                VIEWTYPE_ITEM -> CountryLayoutBinding.inflate(layoutInflater, parent, false)
                    .run { CountryViewHolder(this) }

                else -> throw IllegalStateException("Unsupported view type")
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].itemType
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        data[position].data.let { item ->
            when (holder) {
                is HeaderViewHolder -> holder.bind(item as String)
                is CountryViewHolder -> holder.bind(item as Country)
            }
        }
    }

    class HeaderViewHolder(private val itemBinding: HeaderLayoutBinding) : BaseViewHolder<String>(itemBinding) {
        override fun bind(item: String) {
            itemBinding.headerText.text = item
        }
    }

    class CountryViewHolder(private val itemBinding: CountryLayoutBinding) : BaseViewHolder<Country>(itemBinding) {
        override fun bind(item: Country) {
            with(itemBinding) {
                countryName.text = itemView.context.getString(R.string.country_name, item.name, item.region)
                countryCode.text = item.code
                countryCapitol.text = item.capital
            }
        }
    }
}
