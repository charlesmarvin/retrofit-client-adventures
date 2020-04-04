package io.charlesmarvin.retrofitclientadventures.clients

import com.fasterxml.jackson.annotation.JsonProperty
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class HelloSalutResponse(
    @JsonProperty("code") val code: String,
    @JsonProperty("hello") val hello: String
)

interface FourTonFishClient {
    @GET("/hellosalut")
    suspend fun getSalutation(
        @Header("Authorization") bearerToken: String,
        @Query("cc") countryCode: String
    ): HelloSalutResponse
}

fun newFourTonFishClient(client: OkHttpClient): FourTonFishClient {
    return buildProxy(client)
}

private fun buildProxy(client: OkHttpClient): FourTonFishClient {
    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://fourtonfish.com/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(FourTonFishClient::class.java)
}