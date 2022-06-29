package com.bso.order.domain.repository

import com.bso.order.domain.entity.Order
import java.util.UUID

interface OrderRepository {
    fun findById(id: UUID): Order?
    fun save(order: Order): Order
}
