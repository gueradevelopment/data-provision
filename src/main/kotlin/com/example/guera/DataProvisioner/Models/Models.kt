package com.example.guera.DataProvisioner.Models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.LazyInitializationException
import org.springframework.data.jpa.repository.Temporal
import java.util.*
import javax.persistence.Id
import javax.persistence.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

interface Identified {
    val id: UUID
    fun clear()
}

@Entity
@Table(name = "guerabook")
data class Guerabook(
    @Id
    @Column(name = "id")
    @GeneratedValue
    override val id: UUID = UUID.randomUUID(),
    @Column(name = "title")
    val title: String,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "guerabook", orphanRemoval = true)
    private val boards: List<Board> = listOf()
): Identified {
    fun getBoards(): List<String> = mapToId(boards)

    override fun toString(): String = """
        type: Guerabook
            id: $id
            title: $title
            boards: [${boards.map { it.id.toString() }.fold("") { acc, s ->  "$acc, $s"}}]
    """.trimIndent()

    override fun clear() {
        val field = this::class.declaredMemberProperties.find { it.javaField?.name == "boards" }!!.javaField
        field?.isAccessible = true
        field?.set(this@Guerabook, listOf<Guerabook>())!!
    }

}

@Entity
@Table(name = "board")
data class Board(
    @Id
    @Column(name = "id")
    @GeneratedValue
    override val id: UUID = UUID.randomUUID(),
    @Column(name = "title")
    val title: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_guerabook")
    @JsonIgnore
    var guerabook: Guerabook?,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", orphanRemoval = true)
    private val checklists: List<Checklist> = listOf()
): Identified {
    fun getChecklists(): List<String> = mapToId(checklists)

    override fun clear() {}

    override fun toString(): String = """
        type: Board
            id: $id
            title: $title
            checklists: [${checklists.map { it.id.toString() }.fold("") { acc, s ->  "$acc, $s"}}]
    """.trimIndent()
}

@Entity
@Table(name = "checklist")
data class Checklist(
    @Id
    @Column(name = "id")
    @GeneratedValue
    override val id: UUID = UUID.randomUUID(),
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "completion_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    var completionDate: Date? = null,
    @ManyToOne
    @JoinColumn(name = "fk_board")
    @JsonIgnore
    var board: Board?,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "checklist", orphanRemoval = true)
    private val tasks: List<Task> = listOf()
): Identified {
    fun getTasks(): List<String> = mapToId(tasks)
    @JsonIgnore
    fun tasksProxy(): List<Task> = try {
        tasks
    } catch (e: LazyInitializationException) {
        listOf()
    }

    override fun clear() {}
    override fun toString(): String = """
        type: Checklist
            id: $id
            description: $description
            completionDate: $completionDate
            tasks: [${tasks.map { it.id.toString() }.fold("") { acc, s ->  "$acc, $s"}}]
    """.trimIndent()
}

@Entity
@Table(name = "task")
data class Task(
    @Id
    @Column(name = "id")
    @GeneratedValue
    override val id: UUID = UUID.randomUUID(),
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "completion_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    var completionDate: Date? = null,
    @ManyToOne
    @JoinColumn(name = "fk_checklist")
    @JsonIgnore
    var checklist: Checklist?
): Identified {
    override fun clear() {}
    override fun toString(): String = """
        type: Task
            id: $id
            title: $title
            description: $description
            completionDate: $completionDate
    """.trimIndent()
}

fun <T : Identified> mapToId(collection: List<T>): List<String> = collection.map { it.id.toString() }