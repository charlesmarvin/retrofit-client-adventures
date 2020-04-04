package io.charlesmarvin.retrofitclientadventures.services

class CountryCodeService {
    private val countryCodes = javaClass.getResourceAsStream("/data/country-codes.txt")
        .bufferedReader(Charsets.UTF_8)
        .readLines()
        .toTypedArray()

    fun getRandomCountryCode() = countryCodes.random()
}