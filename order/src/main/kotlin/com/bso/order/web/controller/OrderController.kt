package com.bso.order.web.controller

import com.bso.order.application.usecase.createorder.service.OrderService
import com.bso.order.application.usecase.createorder.CreateOrderUseCase
import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.commons.log.logger
import com.bso.order.domain.entity.Order
import com.bso.order.web.request.OrderRequest
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/order", consumes = ["application/json"], produces = ["application/json"])
class OrderController(
    private val createOrderUseCase: CreateOrderUseCase,
    private val orderService: OrderService
) {
    private val logger: Logger by logger()

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: UUID): Order? = orderService.findById(id)

    @GetMapping
    fun getAll(): List<Order> = orderService.findAll()

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun create(@RequestBody request: OrderRequest) {
        logger.info("[Order] received request to create order with name ${request.name}")
        createOrderUseCase.execute(request.toCreateOrderCommand())
    }

    private fun OrderRequest.toCreateOrderCommand(): CreateOrderCommand = CreateOrderCommand(name = name)
}
