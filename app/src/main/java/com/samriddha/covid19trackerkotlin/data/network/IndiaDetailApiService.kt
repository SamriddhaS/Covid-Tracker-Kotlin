package com.samriddha.covid19trackerkotlin.data.network

import com.samriddha.covid19trackerkotlin.pojo.DistrictName
import com.samriddha.covid19trackerkotlin.pojo.IndiaDetailResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL_1 = "https://api.covid19india.org/"
interface IndiaDetailApiService {

    @GET("data.json")
    suspend fun getDetailIndiaData(): Response<IndiaDetailResponse>

    @GET("state_district_wise.json")
    suspend fun getAllDistricts(): Response<DistrictName>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): IndiaDetailApiService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(DistrictName::class.java,DistrictJsonDeserializerClass())

            return Retrofit.Builder()
                .baseUrl(BASE_URL_1)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build()
                .create(IndiaDetailApiService::class.java)
        }
    }
}