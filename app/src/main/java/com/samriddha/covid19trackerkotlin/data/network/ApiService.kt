package com.samriddha.covid19trackerkotlin.data.network

import com.samriddha.covid19trackerkotlin.data.db.entity.CountryData
import com.samriddha.covid19trackerkotlin.data.db.entity.GlobalData
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://disease.sh/"

interface ApiService {

    @GET("v2/all")
    suspend fun getGlobalData() :Response<GlobalData>

    @GET("v2/countries")
    suspend fun getAllCountryList(@Query("sort") sortId:String="null"):Response<List<CountryData>>

    @GET("v2/countries/india")
    suspend fun getIndiaData(): Response<IndiaData>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ): ApiService {

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(connectivityInterceptor)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
        }
    }

}