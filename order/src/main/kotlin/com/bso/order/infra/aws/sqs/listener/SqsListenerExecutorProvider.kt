package com.bso.order.infra.aws.sqs.listener

import java.util.concurrent.Executor

interface SqsListenerExecutorProvider {
    val executor: Executor
}
