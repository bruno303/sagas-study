package com.bso.order.infra.proxy

import com.bso.order.application.dto.Ticket
import com.bso.order.application.proxy.RestaurantServiceProxy
import com.bso.order.infra.feign.CreateTicketPayload
import com.bso.order.infra.feign.RestaurantFeign
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FeignRestaurantServiceProxy(
    private val restaurantFeign: RestaurantFeign
) : RestaurantServiceProxy {

    override fun createTicket(orderId: UUID): Ticket {
        return restaurantFeign.createTicket(CreateTicketPayload(orderId))
    }

}
