package com.samriddha.covid19trackerkotlin

import android.app.Application
import com.samriddha.covid19trackerkotlin.data.repository.Repository
import com.samriddha.covid19trackerkotlin.ui.global.GlobalViewModelFactory
import com.samriddha.covid19trackerkotlin.data.db.LocalDatabase
import com.samriddha.covid19trackerkotlin.data.network.ApiService
import com.samriddha.covid19trackerkotlin.data.network.ConnectivityInterceptor
import com.samriddha.covid19trackerkotlin.data.network.ConnectivityInterceptorImpl
import com.samriddha.covid19trackerkotlin.data.network.IndiaDetailApiService
import com.samriddha.covid19trackerkotlin.others.SharedPrefManager
import com.samriddha.covid19trackerkotlin.ui.countries.CountriesViewModelFactory
import com.samriddha.covid19trackerkotlin.ui.india.IndiaViewModelFactory
import com.samriddha.covid19trackerkotlin.ui.india.districts.AllDistrictsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CovidTrackerApplication :Application(),KodeinAware{

    override val kodein =  Kodein.lazy {

        import(androidXModule(this@CovidTrackerApplication))

        bind() from singleton { LocalDatabase(instance()) }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { IndiaDetailApiService(instance()) }
        bind() from singleton { instance<LocalDatabase>().globalDataDao()}
        bind() from singleton { instance<LocalDatabase>().allCountryDao()}
        bind() from singleton { instance<LocalDatabase>().indiaDataDao()}
        bind() from singleton { Repository(instance(),instance(),instance(),instance(),instance()) }
        bind() from singleton { SharedPrefManager(instance()) }
        bind() from provider { GlobalViewModelFactory(instance()) }
        bind() from provider { CountriesViewModelFactory(instance(),instance()) }
        bind() from provider { IndiaViewModelFactory(instance()) }
        bind() from provider { AllDistrictsViewModelFactory(instance()) }

    }
}