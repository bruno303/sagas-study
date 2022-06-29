package com.bso.order.application.usecase.createorder.sagas

class SagaStepResult<T> private constructor(val successful: Boolean, val result: T?) {
    companion object {
        fun <T : Any> success(result: T): SagaStepResult<T> = SagaStepResult(true, result)
        fun success(): SagaStepResult<Unit> = SagaStepResult(true, null)
        fun <T> error(): SagaStepResult<T> = SagaStepResult(false, null)
    }
}
