import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun newTasksCanBeAdded() = testApplication {
        application {
            module()
        }

        val response1 = client.post("/tasks"){
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "name" to "Swimming",
                    "description" to "Go to the beach",
                    "priority" to "Low",
                ).formUrlEncode()
            )
        }

        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response2.status)

        val body = response2.bodyAsText()
        assertContains(body, "Swimming")
        assertContains(body, "Go to the beach")
    }

    @Test
    fun getsAllTasks() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        application {
            module()
        }

        val response = client.get("/byPriority/Medium")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(body, "Mow the lawn")
        assertContains(body, "Paint the fence")
    }

    @Test
    fun invalidPriorityProduces400() = testApplication {
        application {
            module()
        }

        val response = client.get("/byPriority/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {
        application {
            module()
        }

        val response = client.get("/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}