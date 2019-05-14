package com.example.guera.DataProvisioner.Models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

interface Identified {
    val id: UUID
    val userId: String
    val isTeamContext: Boolean
}

enum class CompletionState {
    Todo,
    Doing,
    Done
}

@Document(collection = "guerabook")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Guerabook(
    @Id override val id: UUID = UUID.randomUUID(),
    override val userId: String,
    override val isTeamContext: Boolean = false,
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
    override val userId: String,
    override val isTeamContext: Boolean = false,
    @JsonIgnore val parentId: String = "",
    val title: String,
    @DBRef @JsonIgnore val checklists: MutableSet<Checklist> = mutableSetOf()
): Identified {

    @JsonProperty fun getChecklistIds() = checklists.map { it.id.toString() }

    @JsonProperty fun getGuerabookId() = parentId

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
    override val userId: String,
    override val isTeamContext: Boolean = false,
    val title: String,
    @JsonIgnore val parentId: String = "",
    val description: String = "",
    var completionDate: Date = Date.from(Instant.now()),
    val completionState: CompletionState = CompletionState.Todo,
    @DBRef @JsonIgnore val tasks: MutableSet<Task> = mutableSetOf()
): Identified {

    @JsonProperty fun getTaskIds() = tasks.map { it.id.toString() }

    @JsonProperty fun getBoardId() = parentId

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
    override val userId: String,
    override val isTeamContext: Boolean = false,
    val title: String,
    @JsonIgnore val parentId: String = "",
    val description: String = "",
    var completionDate: Date = Date.from(Instant.now()),
    val completionState: CompletionState = CompletionState.Todo
): Identified {

    @JsonProperty fun getChecklistId() = parentId

    override fun toString(): String = """
        type: Task
            id: $id
            title: $title
            description: $description
            completionDate: $completionDate
    """.trimIndent()
}

@Document(collection = "guerateam")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Guerateam(
    @Id override val id: UUID = UUID.randomUUID(),
    override val userId: String,
    override val isTeamContext: Boolean = true,
    val name: String,
    val description: String,
    val ownerId: String,
    val membersId: Set<String> = setOf()
): Identified {

    override fun toString(): String = """
        type: Guerateam
            id: $id
            name: $name
            description: $description
            ownerId: $ownerId
            membersId: $membersId
    """.trimIndent()

}

fun <T : Identified> Set<T>.mapToId(): String = this
    .map { it.id.toString() }
    .fold("") { prev, curr ->  "$prev, $curr"}