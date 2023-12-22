package com.example.countriesdemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesdemo.R
import com.example.countriesdemo.databinding.CountryLayoutBinding
import com.example.countriesdemo.models.Country

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {
    private lateinit var countries: List<Country>

    fun setData(data: List<Country>) {
        countries = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            CountryViewHolder(root)
        }
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
                countryName.text = itemView.context.getString(R.string.country_name, country.name, country.region)
                countryCode.text = country.code
                countryCapitol.text = country.capital
            }
        }
    }
}