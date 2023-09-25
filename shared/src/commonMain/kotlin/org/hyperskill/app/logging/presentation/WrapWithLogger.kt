package org.hyperskill.app.logging.presentation

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Message
import co.touchlab.kermit.Severity
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.reducer.StateReducer

fun <State, Message, Action> Feature<State, Message, Action>.wrapWithLogger(
    logger: Logger,
    tag: String,
    severity: Severity = Severity.Info
): Feature<State, Message, Action> =
    object : Feature<State, Message, Action> by this {
        override fun onNewMessage(message: Message) {
            this@wrapWithLogger.onNewMessage(message)
            val logMessage = "onNewMessage(message=$message)"
            logger.log(severity, tag, throwable = null, message = logMessage)
        }
    }

fun <State, Message, Action> StateReducer<State, Message, Action>.wrapWithLogger(
    logger: Logger,
    tag: String,
    severity: Severity = Severity.Info
): StateReducer<State, Message, Action> =
    object : StateReducer<State, Message, Action> {
        override fun reduce(state: State, message: Message): Pair<State, Set<Action>> {
            val (newState, actions) = this@wrapWithLogger.reduce(state, message)
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
            val logMessage = if(initialState == newState) {
                "reduce(\nstate=$initialState,\nmessage=$message\n)\nNew state = SAME\nActions=$actions"
            } else {
                "reduce(\nstate=PREVIOUS,\nmessage=$message)\nNew state = $newState\nActions=$actions"
            }
            logger.log(severity, tag, throwable = null, logMessage)
        }
    }