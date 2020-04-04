package io.charlesmarvin.retrofitclientadventures.services

import io.charlesmarvin.retrofitclientadventures.clients.AdviceClient
import io.charlesmarvin.retrofitclientadventures.clients.FourTonFishClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class DomainService(
    private val fourTonFishClient: FourTonFishClient,
    private val adviceClient: AdviceClient
) {
    suspend fun getAdvice(name: String, countryCode: String): String =
        coroutineScope {
            val helloSalutResponsePromise =
                async { fourTonFishClient.getSalutation(countryCode) }
            val adviceResponsePromise = async { adviceClient.getAdvice() }
            val salutation = helloSalutResponsePromise.await().hello
            val advice = adviceResponsePromise.await().slip.advice
            "$salutation $name! A word of advice - $advice"
        }
}