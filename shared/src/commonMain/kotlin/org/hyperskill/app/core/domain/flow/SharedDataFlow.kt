package org.hyperskill.app.core.domain.flow

import kotlinx.coroutines.flow.Flow

/**
 * Describes component witch hold shared data
 * and provide the way of its sharing and modification throughout application.
 *
 * Example:
 * ```
 * class SomeEntityFlow : SharedDataFlow<SomeEntity> {
 *
 *     private val someEntityMutableSharedFlow = MutableSharedFlow<SomeEntity>()
 *
 *     override fun observe(): SharedFlow<SomeEntity> = someEntityMutableSharedFlow
 *
 *     override suspend fun notifyDataChanged(data: SomeEntity) {
 *         someEntityMutableSharedFlow.emit(data)
 *     }
 * }
 * ```
 */
interface SharedDataFlow<T : Any?> {
    /**
     * @return [Flow] to observe its changes.
     */
    fun observe(): Flow<T>

    /**
     * Provide ability to change the data presented by [Flow] returned by [observe].
     */
    suspend fun notifyDataChanged(data: T)
}