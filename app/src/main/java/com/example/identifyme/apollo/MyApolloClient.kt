package com.example.identifyme.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MyApolloClient {
    companion object {
        const val BASE_URL = "https://edapi.mingobierno.gob.bo/cudap_api"
    }
    fun setUpApolloClient(): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor(logging)
        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp.build())
            .build()
    }
}