package com.samriddha.covid19trackerkotlin.ui.global

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samriddha.covid19trackerkotlin.data.repository.Repository

class GlobalViewModelFactory(
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GlobalDataViewModel(repository) as T
    }
}