package com.samriddha.covid19trackerkotlin.ui.india

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samriddha.covid19trackerkotlin.data.repository.Repository
import kotlinx.coroutines.async

class IndiaStatViewModel(private val repository: Repository) : ViewModel() {

    val indiaData = viewModelScope.async {
        repository.getIndiaData()
    }

    val indiaDataByTime = viewModelScope.async {
        repository.getIndiaDataByTime()
    }

    val indiaStateData = viewModelScope.async {
        repository.getIndiaStatesData()
    }

    suspend fun fetchIndiaData() {
        repository.updateIndiaDetailData()
    }
}