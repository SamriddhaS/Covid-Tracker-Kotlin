package com.samriddha.covid19trackerkotlin.pojo

import com.google.gson.annotations.SerializedName

data class Districts(
    @SerializedName("active")
    val activeCase: String?,

    @SerializedName("confirmed")
    val totalCase: String?,

    @SerializedName("recovered")
    val recovered: String?,

    @SerializedName("deceased")
    val deaths: String?
)