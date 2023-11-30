package it.macgood.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String, val data : T? = null) : Resource<T>()
}

suspend fun <T> runIo(content: () -> T): T = withContext(Dispatchers.IO) {
    return@withContext content()
}