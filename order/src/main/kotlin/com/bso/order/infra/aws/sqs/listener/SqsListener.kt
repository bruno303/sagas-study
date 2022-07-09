package com.bso.order.infra.aws.sqs.listener

interface SqsListener : SqsListenerExecutorProvider {
    fun listen()
}
