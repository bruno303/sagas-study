package com.bso.order.application.usecase.createorder.sagas.listener.dto.receive

import com.bso.order.domain.entity.Order
import java.util.UUID

data class CreateOrderResponseAsyncSagasDto(
    val endToEndId: UUID,
    val order: Order?,
    val errors: List<String>?
)
