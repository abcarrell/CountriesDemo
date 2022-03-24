package com.acarrell.countriesdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acarrell.countriesdemo.R
import com.acarrell.countriesdemo.api.CountryApi
import com.acarrell.countriesdemo.databinding.ActivityMainBinding
import com.acarrell.countriesdemo.databinding.LayoutCountryItemBinding
import com.acarrell.countriesdemo.repository.Country
import com.acarrell.countriesdemo.repository.RepositoryImpl
import com.acarrell.countriesdemo.repository.Repository
import com.acarrell.countriesdemo.repository.UIState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding
    private val repository: Repository by lazy {
        RepositoryImpl(CountryApi.initialize())
    }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.progressBar.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        binding.countriesList.visibility = View.GONE
        LinearLayoutManager(this).let { layoutManager ->
            binding.countriesList.layoutManager = layoutManager
            binding.countriesList.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        }
        binding.countriesList.adapter = CountryAdapter(arrayListOf()).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
            lifecycleScope.launch {
                viewModel.countryData.observe(this@MainActivity) { uiState ->
                    when (uiState) {
                        is UIState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorText.visibility = View.GONE
                            binding.countriesList.visibility = View.GONE
                        }
                        is UIState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.countriesList.visibility = View.GONE
                            binding.errorText.text = uiState.message
                            binding.errorText.visibility = View.VISIBLE
                        }
                        is UIState.Response -> {
                            (binding.countriesList.adapter as? CountryAdapter)?.apply {
                                setCountries(uiState.response)
                                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                            }
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            binding.countriesList.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    inner class CountryAdapter(private var countryList: List<Country>) : RecyclerView.Adapter<CountryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            return CountryViewHolder(LayoutCountryItemBinding.inflate(LayoutInflater.from(this@MainActivity), parent, false).root)
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