package com.example.traveltrails

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URI.create


class ServerManager {
    val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)
        okHttpClient = builder.build()
    }

    fun test_server(): String {
        val request =
                Request.Builder()
                        .get()
                        .url("http://10.197.28.61:8000/hello")
                        .build()

        val response: Response = okHttpClient.newCall(request).execute()

        val responseBody = response.body?.string()

        var result = "nothing"

        if(response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json = JSONObject(responseBody)
            result = json.getString("message")
        }
        return result
    }
}