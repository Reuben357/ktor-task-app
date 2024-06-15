package com.example.model

//Represents the tasks priority level
enum class Priority{
    Low, Medium, High, Vital
}

//Represents the Task
data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)


//Sends task info to the client inside HTML tables,as table Rows and list of tasks as tables.
fun Task.taskAsRow() = """
    <tr>
        <td>$name</td><td>$description</td><td>$priority</td>
    </tr>
""".trimIndent()

fun List<Task>.taskAsTable() = this.joinToString(
    prefix = "<table rules=\"all\">",
    postfix = "</table>",
    separator = "\n",
    transform = Task::taskAsRow
)


