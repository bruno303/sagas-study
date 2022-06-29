package com.bso.order.application.usecase.createorder.sagas.listener.dto.send

import com.bso.order.domain.entity.Order
import java.util.UUID

data class NotifyAsyncSagasDto(
    val endToEndId: UUID,
    val order: Order,
    val ticketId: UUID,
    val paymentId: UUID
)
