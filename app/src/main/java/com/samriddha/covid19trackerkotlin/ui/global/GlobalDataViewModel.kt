package com.samriddha.covid19trackerkotlin.ui.global

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samriddha.covid19trackerkotlin.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GlobalDataViewModel(private val repository: Repository) : ViewModel() {

    val globalData = viewModelScope.async {
        repository.getGlobalData()
    }

    suspend fun updateGlobalData(){

        withContext(Dispatchers.IO){
            repository.updateGlobalData()
        }

    }

}