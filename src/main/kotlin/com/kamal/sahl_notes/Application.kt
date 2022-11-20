package com.kamal.sahl_notes

import com.kamal.sahl_notes.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DbInit()
    configureRouting()
    configureSecurity()
    configureSerialization()
    configureMonitoring()
    configureHTTP()

}
