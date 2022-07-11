package com.bso.order.application.usecase.createorder.reversal.dto

import java.util.UUID

data class CreateTicketReversalAsyncSagasDto(
    val endToEndId: UUID,
    val orderId: UUID,
    val ticketId: UUID
)
