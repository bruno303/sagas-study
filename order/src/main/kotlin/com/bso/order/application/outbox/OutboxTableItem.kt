package com.bso.order.application.outbox

import java.time.LocalDateTime
import java.util.UUID

data class OutboxTableItem(
    val id: UUID,
    val message: String,
    val queue: String,
    val status: OutboxTableItemStatus = OutboxTableItemStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val sentAt: LocalDateTime? = null
) {
    fun markAsSent(): OutboxTableItem = this.copy(status = OutboxTableItemStatus.SENT, sentAt = LocalDateTime.now())
}

enum class OutboxTableItemStatus {
    PENDING,
    SENT
}
