package com.example.countriesdemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesdemo.R
import com.example.countriesdemo.databinding.CountryLayoutBinding
import com.example.countriesdemo.databinding.HeaderLayoutBinding
import com.example.countriesdemo.entities.Countries
import com.example.countriesdemo.entities.Country
import com.example.countriesdemo.BaseViewHolder
import com.example.countriesdemo.ViewHolderData

class CountriesAdapter : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var countriesData: List<ViewHolderData> = listOf()

    fun setData(data: Countries) {
        notifyItemRangeRemoved(0, countriesData.size)
        countriesData = data.asSequence()
            .sortedBy { it.name }
            .groupBy { it.name.first().toString() }
            .map { entry ->
                listOf(ViewHolderData(entry.key, HEADER_VIEWTYPE)) +
                        entry.value.map { country -> ViewHolderData(country, COUNTRY_VIEWTYPE) }
            }
            .flatten()
        notifyItemRangeInserted(0, countriesData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return LayoutInflater.from(parent.context).let { layoutInflater ->
            when (viewType) {
                HEADER_VIEWTYPE -> HeaderLayoutBinding.inflate(layoutInflater, parent, false)
                    .run { HeaderViewHolder(root) }

                COUNTRY_VIEWTYPE -> CountryLayoutBinding.inflate(layoutInflater, parent, false)
                    .run { CountryViewHolder(root) }

                else -> throw IllegalStateException("Unsupported view type")
            }
        }
    }

    override fun getItemCount(): Int {
        return countriesData.size
    }

    override fun getItemViewType(position: Int): Int {
        return countriesData[position].viewType
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        countriesData[position].item.let { item ->
            when (holder) {
                is HeaderViewHolder -> holder.bind(item as String)
                is CountryViewHolder -> holder.bind(item as Country)
            }
        }
    }

    class HeaderViewHolder(view: View) : BaseViewHolder<String>(view) {
        private val itemBinding = HeaderLayoutBinding.bind(view)

        override fun bind(item: String) {
            itemBinding.headerText.text = item
        }
    }

    class CountryViewHolder(view: View) : BaseViewHolder<Country>(view) {
        private val itemBinding = CountryLayoutBinding.bind(view)

        override fun bind(item: Country) {
            with(itemBinding) {
                countryName.text =
                    itemView.context.getString(R.string.country_name, item.name, item.region)
                countryCode.text = item.code
                countryCapitol.text = item.capital
            }
        }
    }

    companion object {
        private const val HEADER_VIEWTYPE = 0
        private const val COUNTRY_VIEWTYPE = 1
    }
}
