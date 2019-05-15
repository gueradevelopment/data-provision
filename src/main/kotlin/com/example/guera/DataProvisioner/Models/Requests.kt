package com.example.guera.DataProvisioner.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class GuerabookReq(
    val id: UUID,
    val title: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BoardReq(
    val id: UUID,
    val title: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChecklistReq(
    val id: UUID,
    val title: String?,
    val description: String?,
    val completionState: CompletionState?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TaskReq(
    val id: UUID,
    val title: String?,
    val description: String?,
    val completionState: CompletionState?
)