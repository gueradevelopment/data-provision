package com.example.guera.DataProvisioner.Models

import org.springframework.data.jpa.repository.Temporal
import java.util.Date
import javax.persistence.Id
import javax.persistence.*

@Entity
@Table(name = "guerabook")
data class Guerabook (
    @Id
    @Column(name = "id")
    @GeneratedValue
    val id: Long = 0L,
    @Column(name = "title")
    val title: String,
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    val boards: List<Board> = listOf()
)

@Entity
@Table(name = "board")
data class Board (
    @Id
    @Column(name = "id")
    @GeneratedValue
    val id: Long = 0L,
    @Column(name = "title")
    val title: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guerabook_id")
    var guerabook: Guerabook?,
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "checklist_id")
    val checklists: List<Checklist> = listOf()
)

@Entity
@Table(name = "checklist")
data class Checklist (
    @Id
    @Column(name = "id")
    @GeneratedValue
    val id: Long = 0L,
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "completion_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    var completionDate: Date?,
    @ManyToOne
    @JoinColumn(name = "board_id")
    var board: Board?,
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "task_id")
    val tasks: List<Task> = listOf()
)

@Entity
@Table(name = "task")
data class Task (
    @Id
    @Column(name = "id")
    @GeneratedValue
    val id: Long = 0L,
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "completion_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    var completionDate: Date?,
    @ManyToOne
    @JoinColumn(name ="board_id")
    var board: Board?,
    @ManyToOne
    @JoinColumn(name ="checklist_id")
    var checklist: Checklist?
)