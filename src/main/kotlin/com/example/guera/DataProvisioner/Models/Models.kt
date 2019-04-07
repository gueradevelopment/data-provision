package com.example.guera.DataProvisioner.Models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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
    @Id override val id: UUID = UUID.randomUUID(),
    val title: String,
    @DBRef @JsonIgnore val boards: MutableSet<Board> = mutableSetOf()
): Identified {

    @JsonProperty fun getBoardIds() = this.boards.map { it.id.toString() }

    override fun toString(): String = """
        type: Guerabook
            id: $id
            title: $title
            boards: [${boards.mapToId()}]
    """.trimIndent()

}

@Document(collection = "board")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Board(
    @Id override val id: UUID = UUID.randomUUID(),
    val title: String,
    @DBRef @JsonIgnore val checklists: MutableSet<Checklist> = mutableSetOf()
): Identified {

    @JsonProperty fun getChecklistIds() = checklists.map { it.id.toString() }

    override fun toString(): String = """
        type: Board
            id: $id
            title: $title
            checklists: [${checklists.mapToId()}}]
    """.trimIndent()

}

@Document(collection = "checklist")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Checklist(
    @Id override val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    var completionDate: Date? = null,
    @DBRef @JsonIgnore val tasks: MutableSet<Task> = mutableSetOf()
): Identified {

    @JsonProperty fun getTaskIds() = tasks.map { it.id.toString() }

    override fun toString(): String = """
        type: Checklist
            id: $id
            description: $description
            completionDate: $completionDate
            tasks: [${tasks.mapToId()}]
    """.trimIndent()

}

@Document(collection = "task")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Task(
    @Id override val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    var completionDate: Date? = null
): Identified {

    override fun toString(): String = """
        type: Task
            id: $id
            title: $title
            description: $description
            completionDate: $completionDate
    """.trimIndent()
}

fun <T : Identified> Set<T>.mapToId(): String = this
    .map { it.id.toString() }
    .fold("") { acc, s ->  "$acc, $s"}