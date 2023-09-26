package org.hyperskill.app.logging.presentation

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import org.hyperskill.app.core.domain.BuildVariant
import ru.nobird.app.presentation.redux.reducer.StateReducer

/**
 * Log reduce results (new state & actions) when application is not in the release mode
 */
fun <State, Message, Action> StateReducer<State, Message, Action>.wrapWithLogger(
    buildVariant: BuildVariant,
    logger: Logger,
    tag: String,
    severity: Severity = Severity.Debug
): StateReducer<State, Message, Action> =
    if (buildVariant == BuildVariant.RELEASE) {
        this
    } else {
        LoggableStateReducer(
            origin = this,
            logger = logger,
            tag = tag,
            severity = severity
        )
    }

private class LoggableStateReducer<State, Message, Action>(
    private val origin: StateReducer<State, Message, Action>,
    private val logger: Logger,
    private val tag: String,
    private val severity: Severity = Severity.Info
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> {
        val (newState, actions) = origin.reduce(state, message)
        logChanges(
            message = message,
            initialState = state,
            newState = newState,
            actions = actions
        )
        return newState to actions
    }

    private fun logChanges(
        message: Message,
        initialState: State,
        newState: State,
        actions: Set<Action>
    ) {
        logger.logBlock(severity, tag, throwable = null) {
            if (initialState == newState) {
                "reduce(\nmessage=$message,\nstate=$initialState\n)\nActions=$actions\nNew state = SAME"
            } else {
                "reduce(\nmessage=$message,\nstate=PREVIOUS\n)\nActions=$actions\nNew state = $newState"
            }
        }
    }
}