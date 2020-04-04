package io.charlesmarvin.retrofitclientadventures.clients

import com.fasterxml.jackson.annotation.JsonProperty
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

data class AdviceResponse(
    @JsonProperty("slip") val slip: AdviceSplit
)

data class AdviceSplit(
    @JsonProperty("advice") val advice: String,
    @JsonProperty("slip_id") val slipId: String
)

interface AdviceClient {
    @GET("/advice")
    suspend fun getAdvice(
        @Header("Authorization") bearerToken: String
    ): AdviceResponse
}

fun newAdviceClient(client: OkHttpClient): AdviceClient {
    return buildProxy(client)
}

private fun buildProxy(client: OkHttpClient): AdviceClient {
    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.adviceslip.com/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(AdviceClient::class.java)
}