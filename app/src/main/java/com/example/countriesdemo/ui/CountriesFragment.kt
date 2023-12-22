package com.example.countriesdemo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countriesdemo.R
import com.example.countriesdemo.databinding.FragmentCountryBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CountriesFragment : Fragment(R.layout.fragment_country) {
    private val binding: FragmentCountryBinding by lazy {
        FragmentCountryBinding.inflate(layoutInflater)
    }
    private val viewModel: CountriesViewModel by viewModels()
    private val adapter: CountriesAdapter by lazy { CountriesAdapter() }
    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.apply {
            countriesList.layoutManager = layoutManager
            countriesList.adapter = adapter
        }.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { uiState ->
                        with(binding) {
                            progressBar.visibility = if (uiState.loading) View.VISIBLE else View.GONE
                            adapter.setData(uiState.countries)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                launch {
                    viewModel.effects.collect { effect ->
                        when (effect) {
                            is CountriesViewModel.Effect.CompleteMessage -> showMessage("Loading complete")
                            is CountriesViewModel.Effect.ErrorMessage -> showMessage("Error: ${effect.message}")
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LAYOUT_MANAGER_STATE, binding.countriesList.layoutManager?.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        @Suppress("DEPRECATION")
        savedInstanceState?.getParcelable<Parcelable>(LAYOUT_MANAGER_STATE)?.run {
            binding.countriesList.layoutManager?.onRestoreInstanceState(this)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(requireActivity().window.decorView, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val LAYOUT_MANAGER_STATE = "classname.recycler.layout"
    }
}
