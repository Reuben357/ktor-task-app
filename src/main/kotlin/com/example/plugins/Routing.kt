package com.example.plugins

import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskRepository
import com.example.model.taskAsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        post("/tasks") {
            val formContent = call.receiveParameters()

            val params = Triple(
                formContent["name"] ?: "",
                formContent["description"] ?: "",
                formContent["priority"] ?: ""
            )

            if (params.toList().any{ it.isEmpty()}){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            try {
                val priority = Priority.valueOf(params.third)
                TaskRepository.addTask(
                    Task(
                        params.first,
                        params.second,
                        priority
                    )
                )

                call.respond(HttpStatusCode.NoContent)
            } catch (ex: IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        staticResources("/task-ui", "task-ui")

        route("/tasks") {
            get {
                val tasks = TaskRepository.allTasks()
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.taskAsTable()
                )
            }
        }

        get("/byName/{taskName}"){
            val name = call.parameters["taskName"]
            if (name == null){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val task = TaskRepository.taskByName(name)
            if (task == null){
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = listOf(task).taskAsTable()
            )
        }

        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.taskByPriority(priority)

                if (tasks.isEmpty()){
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.taskAsTable()
                )
            } catch (ex: IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
