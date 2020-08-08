package com.samriddha.covid19trackerkotlin.others

import java.io.IOException

class NoInternetException(massage:String):IOException(massage)
class ApiException(massage: String):IOException(massage)