package com.tc.countries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.google.android.material.snackbar.Snackbar
import com.tc.countries.R
import com.tc.countries.databinding.FragmentCountryBinding
import com.tc.countries.ui.CountriesViewModel.Effect
import kotlinx.coroutines.launch

class CountriesFragment : Fragment(R.layout.fragment_country) {
    // This method of creating the binding is used to allow us to null out binding after view destruction
    // and so avoid memory leaks
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountriesViewModel by viewModels { CountriesViewModel.create() }

    private val adapter: CountriesAdapter by lazy {
        CountriesAdapter().apply {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCountryBinding.inflate(inflater).apply {
            countriesList.layoutManager = layoutManager
            countriesList.adapter = adapter
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { uiState ->
                        with(binding) {
                            progressBar.visibility =
                                if (uiState.loading) View.VISIBLE else View.GONE
                            adapter.data = uiState.countries
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
        binding.filterValue.doOnTextChanged { text, _, _, _ ->
            viewModel.filterCountriesByName(text ?: "")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
