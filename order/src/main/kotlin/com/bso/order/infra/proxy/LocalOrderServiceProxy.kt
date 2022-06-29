package com.bso.order.infra.proxy

import com.bso.order.application.proxy.OrderServiceProxy
import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.application.usecase.createorder.service.OrderService
import com.bso.order.domain.entity.Order
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LocalOrderServiceProxy(
    private val orderService: OrderService
) : OrderServiceProxy {
    override fun create(createCmd: CreateOrderCommand): Order = orderService.create(createCmd)
    override fun cancelOrder(orderId: UUID) = orderService.cancelOrder(orderId)
}
