package com.samriddha.covid19trackerkotlin.data.network

import com.samriddha.covid19trackerkotlin.others.ApiException
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {

    suspend fun <T:Any> safeApiRequest(call:suspend() -> Response<T>):T{

        val response = call.invoke()
        if (response.isSuccessful)
            return response.body()!!
        else{
            val massage= StringBuilder()
            massage.append("Error Code:${response.code()}")
            throw ApiException(massage.toString())
        }

    }

}