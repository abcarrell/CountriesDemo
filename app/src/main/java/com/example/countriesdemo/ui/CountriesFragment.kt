package com.example.countriesdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.example.countriesdemo.R
import com.example.countriesdemo.databinding.FragmentCountryBinding
import com.example.countriesdemo.ui.CountriesViewModel.Effect
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CountriesFragment : Fragment(R.layout.fragment_country) {
    private val binding: FragmentCountryBinding by lazy {
        FragmentCountryBinding.inflate(layoutInflater)
    }
    private val viewModel: CountriesViewModel by viewModels()
    private val adapter: CountriesAdapter by lazy {
        CountriesAdapter().apply {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.apply {
            countriesList.layoutManager = layoutManager
            countriesList.adapter = adapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { uiState ->
                        with(binding) {
                            progressBar.visibility = if (uiState.loading) View.VISIBLE else View.GONE
                            adapter.setData(uiState.countries)
                            adapter.notifyItemRangeChanged(0, uiState.countries.size)
                        }
                    }
                }
                launch {
                    viewModel.effects.collect { effect ->
                        when (effect) {
                            is Effect.CompleteMessage -> showMessage("Loading complete")
                            is Effect.ErrorMessage -> showMessage("Error: ${effect.message}")
                        }
                    }
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(requireActivity().window.decorView, message, Snackbar.LENGTH_LONG).show()
    }
}
