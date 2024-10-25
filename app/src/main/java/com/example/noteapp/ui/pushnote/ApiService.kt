package com.example.noteapp.ui.pushnote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("your-endpoint")
    fun sendToken(@Body token: String): Call<ResponseBody>
}

