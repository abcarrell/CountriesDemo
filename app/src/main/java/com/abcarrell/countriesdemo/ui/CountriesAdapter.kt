package com.abcarrell.countriesdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcarrell.countriesdemo.BaseViewHolder
import com.abcarrell.countriesdemo.R
import com.abcarrell.countriesdemo.ViewHolderData
import com.abcarrell.countriesdemo.databinding.CountryLayoutBinding
import com.abcarrell.countriesdemo.databinding.HeaderLayoutBinding
import com.abcarrell.countriesdemo.entities.Countries
import com.abcarrell.countriesdemo.entities.Country

class CountriesAdapter : RecyclerView.Adapter<BaseViewHolder<*>>() {
    var data: Countries = listOf()
        set(value) {
            notifyItemRangeRemoved(0, countriesData.size)
            field = value
            notifyItemRangeInserted(0, countriesData.size)
        }
    private val countriesData: List<ViewHolderData>
        get() = data.asSequence()
            .sortedWith(compareBy<Country> { it.region }.thenBy { it.code })
            .groupBy { it.region }.asSequence()
            .map { entry ->
                listOf(ViewHolderData(entry.key, HEADER_VIEWTYPE)) +
                        entry.value.map { country -> ViewHolderData(country, COUNTRY_VIEWTYPE) }
            }.flatten().toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return LayoutInflater.from(parent.context).let { layoutInflater ->
            when (viewType) {
                HEADER_VIEWTYPE -> HeaderLayoutBinding.inflate(layoutInflater, parent, false)
                    .run { HeaderViewHolder(this) }

                COUNTRY_VIEWTYPE -> CountryLayoutBinding.inflate(layoutInflater, parent, false)
                    .run { CountryViewHolder(this) }

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

    class HeaderViewHolder(private val itemBinding: HeaderLayoutBinding) : BaseViewHolder<String>(itemBinding) {
        override fun bind(item: String) {
            itemBinding.headerText.text = item
        }
    }

    class CountryViewHolder(private val itemBinding: CountryLayoutBinding) : BaseViewHolder<Country>(itemBinding) {
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
