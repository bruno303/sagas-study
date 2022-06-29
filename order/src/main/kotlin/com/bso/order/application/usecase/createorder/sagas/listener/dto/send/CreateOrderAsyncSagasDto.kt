package com.bso.order.application.usecase.createorder.sagas.listener.dto.send

import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import java.util.UUID

data class CreateOrderAsyncSagasDto(
    val endToEndId: UUID,
    val createOrderCommand: CreateOrderCommand
)
