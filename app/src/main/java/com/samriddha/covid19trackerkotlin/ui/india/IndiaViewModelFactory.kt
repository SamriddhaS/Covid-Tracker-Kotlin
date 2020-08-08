package com.samriddha.covid19trackerkotlin.ui.india

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samriddha.covid19trackerkotlin.data.repository.Repository

class IndiaViewModelFactory(
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IndiaStatViewModel(repository) as T
    }
}