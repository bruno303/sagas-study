package com.bso.order.infra.repository

import com.bso.order.domain.entity.Order
import com.bso.order.domain.repository.OrderRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class InMemoryOrderRepository : OrderRepository {
    private val orders: MutableList<Order> = mutableListOf()

    override fun findById(id: UUID): Order? {
        return orders.firstOrNull { it.id == id }
    }

    override fun save(order: Order): Order {
        findById(order.id)?.let {
            orders.set(orders.indexOf(it), order)
        } ?: orders.add(order)
        return order
    }
}
