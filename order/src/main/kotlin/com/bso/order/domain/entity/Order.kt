package com.bso.order.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Order(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val datetime: LocalDateTime = LocalDateTime.now(),
    val status: OrderStatus = OrderStatus.CREATED,
    val error: String? = null
)

enum class OrderStatus {
    CREATED,
    TICKET_PENDING,
    PAYMENT_PENDING,
    NOTIFICATION_PENDING,
    SUCCESS,
    CANCELED,
    FAILURE
}
