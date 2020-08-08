package com.samriddha.covid19trackerkotlin.ui.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samriddha.covid19trackerkotlin.data.repository.Repository
import com.samriddha.covid19trackerkotlin.others.SharedPrefManager

class CountriesViewModelFactory(
    private val repository: Repository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountriesViewModel(repository,sharedPrefManager) as T
    }
}