package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/tasks") {
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                    <h3> TODO: </h3>
                    <ol>
                    <li>A table of all the tasks</li>
                    <li>A form to submit new tasks</li>
                </ol>
                """.trimIndent()
            )
        }
    }
}
