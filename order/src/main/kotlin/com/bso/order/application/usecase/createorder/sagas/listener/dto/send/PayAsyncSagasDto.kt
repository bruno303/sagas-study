package com.bso.order.application.usecase.createorder.sagas.listener.dto.send

import java.util.UUID

data class PayAsyncSagasDto(
    val endToEndId: UUID,
    val orderId: UUID,
    val ticketId: UUID
)
