package com.samriddha.covid19trackerkotlin.ui.countries

import androidx.lifecycle.*
import com.samriddha.covid19trackerkotlin.data.repository.Repository
import com.samriddha.covid19trackerkotlin.others.SharedPrefManager
import kotlinx.coroutines.async

class CountriesViewModel(
    private val repository: Repository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    val allCountryList = viewModelScope.async {
        repository.getAllCountry()
    }

    suspend fun updateAllCountry(keyChanged: Boolean) {

        val currentKey = sharedPrefManager.getCurrentKey()!!

        if (keyChanged) {
            repository.updateAllCountry(currentKey, true)
            return
        }

        repository.updateAllCountry(currentKey, false)

    }

}