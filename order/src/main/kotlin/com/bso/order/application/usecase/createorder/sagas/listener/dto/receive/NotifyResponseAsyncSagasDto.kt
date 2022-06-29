package com.bso.order.application.usecase.createorder.sagas.listener.dto.receive

import com.bso.order.application.dto.Payment
import com.bso.order.domain.entity.Order
import java.util.UUID

data class NotifyResponseAsyncSagasDto(
    val endToEndId: UUID,
    val order: Order,
    val ticketId: UUID,
    val paymentId: UUID,
    val notificationId: UUID,
    val errors: List<String>?
)
