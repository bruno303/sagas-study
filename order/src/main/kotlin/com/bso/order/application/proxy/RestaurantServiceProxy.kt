package com.bso.order.application.proxy

import com.bso.order.application.dto.Ticket
import java.util.UUID

interface RestaurantServiceProxy {
    fun createTicket(orderId: UUID): Ticket
}
