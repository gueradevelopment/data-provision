package com.example.guera.DataProvisioner.Models

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
    val title: String
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
    val guerabook: Guerabook
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
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board
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
    @ManyToOne
    @JoinColumn(name ="board_id")
    val board: Board,
    @ManyToOne
    @JoinColumn(name ="checklist_id")
    val checklist: Checklist
)