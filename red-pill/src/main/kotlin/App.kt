package io.charlesmarvin.retrofitclientadventures

import com.codahale.metrics.jmx.JmxReporter
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.charlesmarvin.retrofitclientadventures.clients.newAdviceClient
import io.charlesmarvin.retrofitclientadventures.clients.newFourTonFishClient
import io.charlesmarvin.retrofitclientadventures.services.CountryCodeService
import io.charlesmarvin.retrofitclientadventures.services.DomainService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.metrics.dropwizard.DropwizardMetrics
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

object Module {
    val domainService by lazy {
        DomainService(
            adviceClient = newAdviceClient(),
            fourTonFishClient = newFourTonFishClient()
        )
    }
    val countryCode by lazy {
        CountryCodeService()
    }
}

fun Application.appModule() {
    val domainService: DomainService = Module.domainService
    val countryCode: CountryCodeService = Module.countryCode
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            this.registerModule(KotlinModule())
        }
    }
    install(DropwizardMetrics) {
        JmxReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build()
            .start()
    }
    install(Routing) {
        routing {
            get("/") {
                val name = requireNotNull(call.parameters["name"]) {
                    "name parameter is required"
                }
                val response = domainService.getAdvice(name, countryCode.getRandomCountryCode())
                call.respondText(response)
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, 8080, module = Application::appModule).start(wait = true)
}
