package com.bso.order.application.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Payment(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: BigDecimal,
    val paymentDateTime: LocalDateTime = LocalDateTime.now()
)
