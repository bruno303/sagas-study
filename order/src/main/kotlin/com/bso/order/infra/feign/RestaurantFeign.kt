package com.bso.order.infra.feign

import com.bso.order.application.dto.Ticket
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import java.util.UUID

@FeignClient(name = "restaurantFeign", url = "http://localhost:3000")
interface RestaurantFeign {

    @PostMapping("ticket", produces = ["application/json"], consumes = ["application/json"])
    fun createTicket(payload: CreateTicketPayload): Ticket

}

data class CreateTicketPayload(
    val orderId: UUID
)
