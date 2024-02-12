package com.tc.countries.ui.component

import com.tc.countries.ui.CountriesViewModel
import com.tc.mvi.MVIActor
import com.tc.mvi.mvi

object CountriesViewModule {
    fun provideCountriesMvi(): MVIActor<CountriesViewModel.UIState, Nothing, CountriesViewModel.Effect> =
        mvi(CountriesViewModel.UIState())
}
