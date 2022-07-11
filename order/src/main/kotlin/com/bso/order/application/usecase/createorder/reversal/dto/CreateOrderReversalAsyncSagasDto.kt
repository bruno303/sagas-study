package com.bso.order.application.usecase.createorder.reversal.dto

import java.util.UUID

data class CreateOrderReversalAsyncSagasDto(
    val endToEndId: UUID,
    val orderId: UUID
)
