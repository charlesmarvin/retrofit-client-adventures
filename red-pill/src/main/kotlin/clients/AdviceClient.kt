package io.charlesmarvin.retrofitclientadventures.clients

import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET

data class AdviceResponse(
    @JsonProperty("slip") val slip: AdviceSplit
)

data class AdviceSplit(
    @JsonProperty("advice") val advice: String,
    @JsonProperty("slip_id") val slipId: String
)

interface AdviceClient {
    @GET("/advice")
    suspend fun getAdvice(): AdviceResponse
}

private val authInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader(HttpHeaders.UserAgent, "ktor experiment")
        .addHeader(HttpHeaders.Authorization, "Bearer my-advice-token")
        .build()
    chain.proceed(request)
}


fun newAdviceClient(): AdviceClient {
    val apiClient = OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    return buildProxy(apiClient)
}

private fun buildProxy(client: OkHttpClient): AdviceClient {
    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.adviceslip.com/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(AdviceClient::class.java)
}