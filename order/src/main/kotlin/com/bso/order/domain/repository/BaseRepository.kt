package com.bso.order.domain.repository

interface BaseRepository<I, T> {
    fun findById(id: I): T?
    fun save(entity: T): T
}
