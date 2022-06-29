package com.bso.order.application.usecase.createorder.sagas.listener.dto.send

import java.util.UUID

data class CreateTicketAsyncSagasDto(
    val endToEndId: UUID,
    val orderId: UUID
)

