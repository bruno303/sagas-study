package com.bso.order.application.dto

import java.time.LocalDateTime
import java.util.UUID

data class Ticket(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val creationDateTime: LocalDateTime = LocalDateTime.now()
)
