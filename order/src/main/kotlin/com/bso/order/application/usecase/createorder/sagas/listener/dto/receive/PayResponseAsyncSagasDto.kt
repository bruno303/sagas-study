package com.bso.order.application.usecase.createorder.sagas.listener.dto.receive

import com.bso.order.application.dto.Payment
import java.util.UUID

data class PayResponseAsyncSagasDto(
    val endToEndId: UUID,
    val orderId: UUID,
    val ticketId: UUID,
    val payment: Payment?,
    val errors: List<String>?
)
