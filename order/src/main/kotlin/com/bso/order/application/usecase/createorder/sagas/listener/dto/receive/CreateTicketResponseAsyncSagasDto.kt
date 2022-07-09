package com.bso.order.application.usecase.createorder.sagas.listener.dto.receive

import com.bso.order.application.dto.Ticket
import java.util.UUID

data class CreateTicketResponseAsyncSagasDto(
    val endToEndId: UUID,
    val orderId: UUID,
    val ticket: Ticket?,
    val errors: List<String>?
)

