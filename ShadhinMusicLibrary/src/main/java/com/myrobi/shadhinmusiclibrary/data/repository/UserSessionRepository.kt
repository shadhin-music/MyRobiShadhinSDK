package com.myrobi.shadhinmusiclibrary.data.repository

interface UserSessionRepository {
    suspend fun start()
    suspend fun end()
}