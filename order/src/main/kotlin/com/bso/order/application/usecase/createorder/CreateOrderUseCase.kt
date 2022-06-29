package com.bso.order.application.usecase.createorder

import com.bso.order.application.usecase.createorder.command.CreateOrderCommand
import com.bso.order.application.usecase.createorder.sagas.CreateOrderAsyncSagaManager
import org.springframework.stereotype.Component

@Component
class CreateOrderUseCase(
    private val sagaManager: CreateOrderAsyncSagaManager
) {
    fun execute(createCmd: CreateOrderCommand) {
        sagaManager.execute(createCmd = createCmd)
    }
}
