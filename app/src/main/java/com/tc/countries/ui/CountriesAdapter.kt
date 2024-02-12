package com.tc.countries.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tc.countries.R
import com.tc.countries.databinding.CountryLayoutBinding
import com.tc.countries.entities.Countries
import com.tc.countries.entities.Country

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {
    var data: Countries = listOf()
        set(value) {
            notifyItemRangeRemoved(0, data.size)
            field = value
            notifyItemRangeInserted(0, data.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return LayoutInflater.from(parent.context).let { layoutInflater ->
            CountryLayoutBinding.inflate(layoutInflater, parent, false).run {
                CountryViewHolder(this)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class CountryViewHolder(private val itemBinding: CountryLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Country) {
            with(itemBinding) {
                countryName.text = itemView.context.getString(R.string.country_name, item.name, item.region)
                countryCode.text = item.code
                countryCapitol.text = item.capital
            }
        }
    }
}
