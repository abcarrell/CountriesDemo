package com.acarrell.countriesdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acarrell.countriesdemo.CountriesDemo
import com.acarrell.countriesdemo.R
import com.acarrell.countriesdemo.databinding.ActivityMainBinding
import com.acarrell.countriesdemo.databinding.LayoutCountryItemBinding
import com.acarrell.countriesdemo.repository.Country
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    init {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.events.collectLatest { viewEvent ->
                    when (viewEvent) {
                        is MainViewModel.ViewEvent.Started -> {
                            binding.countriesList.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MainViewModel.ViewEvent.Complete -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            binding.countriesList.visibility = View.VISIBLE
                        }
                        is MainViewModel.ViewEvent.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.countriesList.visibility = View.GONE
                            binding.errorText.apply {
                                text = viewEvent.message
                                visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
            viewModel.countryData.observe(this@MainActivity) { countries ->
                (binding.countriesList.adapter as? CountryAdapter)?.apply {
                    setCountries(countries)
                    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
                binding.progressBar.visibility = View.GONE
                binding.errorText.visibility = View.GONE
                binding.countriesList.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        LinearLayoutManager(this).let { layoutManager ->
            binding.countriesList.layoutManager = layoutManager
            binding.countriesList.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        }
        binding.countriesList.adapter = CountryAdapter(arrayListOf()).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        }
    }

    inner class CountryAdapter(private var countryList: List<Country>) : RecyclerView.Adapter<CountryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            return LayoutCountryItemBinding.inflate(LayoutInflater.from(this@MainActivity), parent, false).let { CountryViewHolder(it.root) }
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bind(countryList[position])
        }

        override fun getItemCount(): Int {
            return countryList.size
        }

        fun setCountries(countries: List<Country>) {
            countryList = countries
        }
    }

    inner class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemBinding = LayoutCountryItemBinding.bind(view)

        fun bind(country: Country) {
            itemBinding.countryNameRegion.text = itemView.context.getString(R.string.country_name_region, country.name, country.region)
            itemBinding.countryCode.text = country.code
            itemBinding.countryCapital.text = country.capital
        }
    }
}