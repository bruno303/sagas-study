package com.bso.order.application.proxy

import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.domain.entity.Order
import java.util.UUID

interface OrderServiceProxy {
    fun create(createCmd: CreateOrderCommand): Order
    fun cancelOrder(orderId: UUID)
}
