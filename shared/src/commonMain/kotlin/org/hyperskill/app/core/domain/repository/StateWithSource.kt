package org.hyperskill.app.core.domain.repository

import org.hyperskill.app.core.domain.DataSourceType

/**
 * Contains current [State] and [DataSourceType] witch was used to get the state.
 * @see [StateRepository.getStateWithSource]
 */
data class StateWithSource<State : Any>(
    val state: State,
    val usedDataSourceType: DataSourceType
)