package com.zozuliak

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.zozuliak.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "192.168.0.52", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
