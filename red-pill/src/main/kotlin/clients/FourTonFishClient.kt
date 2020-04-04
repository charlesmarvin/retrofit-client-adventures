package io.charlesmarvin.retrofitclientadventures.clients

import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.HttpHeaders
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class HelloSalutResponse(
    @JsonProperty("code") val code: String,
    @JsonProperty("hello") val hello: String
)

interface FourTonFishClient {
    @GET("/hellosalut")
    suspend fun getSalutation(@Query("cc") countryCode: String): HelloSalutResponse
}

private val authInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader(HttpHeaders.UserAgent, "ktor experiment")
        .addHeader(HttpHeaders.Authorization, "Bearer my-four-ton-fish")
        .build()
    chain.proceed(request)
}


fun newFourTonFishClient(): FourTonFishClient {
    val apiClient = OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    return buildProxy(apiClient)
}

private fun buildProxy(client: OkHttpClient): FourTonFishClient {
    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://fourtonfish.com/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(FourTonFishClient::class.java)
}