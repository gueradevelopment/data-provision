package com.example.guera.DataProvisioner.Models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

interface Identified {
    val id: UUID
}

@Document(collection = "guerabook")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Guerabook(
    @Id
    override val id: UUID = UUID.randomUUID(),
    val title: String,
    @DBRef
    val boards: List<Board> = listOf()
): Identified {

    override fun toString(): String = """
        type: Guerabook
            id: $id
            title: $title
            boards: [${boards.map { it.id.toString() }.fold("") { acc, s ->  "$acc, $s"}}]
    """.trimIndent()

}

@Document(collection = "board")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Board(
    @Id
    override val id: UUID = UUID.randomUUID(),
    val title: String,
    @JsonIgnore
    var guerabook: Guerabook?,
    @DBRef
    val checklists: List<Checklist> = listOf()
): Identified {

    override fun toString(): String = """
        type: Board
            id: $id
            title: $title
            checklists: [${checklists.map { it.id.toString() }.fold("") { acc, s ->  "$acc, $s"}}]
    """.trimIndent()

}

@Document(collection = "checklist")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Checklist(
    @Id
    override val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    var completionDate: Date? = null,
    @JsonIgnore
    var board: Board?,
    @DBRef
    val tasks: List<Task> = listOf()
): Identified {

    override fun toString(): String = """
        type: Checklist
            id: $id
            description: $description
            completionDate: $completionDate
            tasks: [${tasks.map { it.id.toString() }.fold("") { acc, s ->  "$acc, $s"}}]
    """.trimIndent()

}

@Document(collection = "task")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Task(
    @Id
    override val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    var completionDate: Date? = null,
    @JsonIgnore
    var checklist: Checklist?
): Identified {

    override fun toString(): String = """
        type: Task
            id: $id
            title: $title
            description: $description
            completionDate: $completionDate
    """.trimIndent()
}

fun <T : Identified> mapToId(collection: List<T>): List<String> = collection.map { it.id.toString() }