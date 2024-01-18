package com.example.countriesdemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesdemo.R
import com.example.countriesdemo.databinding.CountryLayoutBinding
import com.example.countriesdemo.entities.entities.Countries
import com.example.countriesdemo.entities.Country

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {
    private val countries: MutableList<Country> = mutableListOf()

    fun setData(data: Countries) {
        with(countries) {
            size.let { oldSize ->
                clear()
                notifyItemRangeRemoved(0, oldSize)
            }
            addAll(data)
            notifyItemRangeInserted(0, data.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { CountryViewHolder(root) }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemBinding: CountryLayoutBinding = CountryLayoutBinding.bind(view)

        fun bind(country: Country) {
            with(itemBinding) {
                countryName.text =
                    itemView.context.getString(R.string.country_name, country.name, country.region)
                countryCode.text = country.code
                countryCapitol.text = country.capital
            }
        }
    }
}
