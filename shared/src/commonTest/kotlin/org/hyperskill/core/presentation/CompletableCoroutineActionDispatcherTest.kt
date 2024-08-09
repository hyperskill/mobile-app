package org.hyperskill.core.presentation

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hyperskill.app.core.presentation.CompletableCoroutineActionDispatcher
import org.hyperskill.app.core.presentation.CompletableCoroutineActionDispatcherConfig
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature
import ru.nobird.app.presentation.redux.reducer.StateReducer

class CompletableCoroutineActionDispatcherTest {

    @Test
    fun `Action handling should not be cancelled if the feature is cancelled`() {
        runBlocking {
            val actionDispatcherScope =
                CompletableCoroutineActionDispatcherConfig(this).createScope()

            val actualHandledActions = mutableListOf<TestFeature.Action>()

            val actionDispatcher =
                object : CompletableCoroutineActionDispatcher<TestFeature.Action, TestFeature.Message>(
                    coroutineScope = actionDispatcherScope
                ) {
                    override suspend fun handleNonCancellableAction(action: TestFeature.Action) {
                        delay(5)
                        actualHandledActions.add(action)
                    }
                }

            val feature = ReduxFeature(
                initialState = TestFeature.State,
                reducer = TestReducer()
            ).wrapWithActionDispatcher(actionDispatcher)

            feature.addActionListener { action ->
                if (action is TestFeature.Action.CancelFeatureAction) {
                    feature.cancel()
                }
            }

            feature.onNewMessage(TestFeature.Message.ProduceThreeActionsAndCancel)

            // Wait for actionDispatcher tasks to be finished
            actionDispatcherScope.coroutineContext[Job]?.join()

            val expectedHandledActions = listOf(
                TestFeature.Action.Action1,
                TestFeature.Action.Action2,
                TestFeature.Action.Action3,
                TestFeature.Action.CancelFeatureAction
            )

            assertContentEquals(
                expected = expectedHandledActions,
                actual = actualHandledActions
            )
        }
    }

    @Test
    fun `Actions produced after feature cancellation should not be handled`() {
        runBlocking {
            val actionDispatcherScope =
                CompletableCoroutineActionDispatcherConfig(this).createScope()

            val actualHandledActions = mutableListOf<TestFeature.Action>()

            val actionDispatcher =
                object : CompletableCoroutineActionDispatcher<TestFeature.Action, TestFeature.Message>(
                    coroutineScope = actionDispatcherScope
                ) {
                    override suspend fun handleNonCancellableAction(action: TestFeature.Action) {
                        delay(5)
                        actualHandledActions.add(action)
                    }
                }

            val feature = ReduxFeature(
                initialState = TestFeature.State,
                reducer = TestReducer()
            ).wrapWithActionDispatcher(actionDispatcher)

            feature.addActionListener { action ->
                if (action is TestFeature.Action.CancelFeatureAction) {
                    feature.cancel()
                }
            }

            feature.onNewMessage(TestFeature.Message.ProduceThreeActionsWithCancellationInTheMiddle)

            // Wait for actionDispatcher tasks to be finished
            actionDispatcherScope.coroutineContext[Job]?.join()

            val expectedHandledActions = listOf(
                TestFeature.Action.Action1,
                TestFeature.Action.CancelFeatureAction
            )

            assertContentEquals(
                expected = expectedHandledActions,
                actual = actualHandledActions
            )
        }
    }
}

private object TestFeature {
    object State

    sealed interface Message {
        data object ProduceThreeActionsAndCancel : Message
        data object ProduceThreeActionsWithCancellationInTheMiddle : Message
    }

    sealed interface Action {
        data object Action1 : Action
        data object Action2 : Action
        data object Action3 : Action

        data object CancelFeatureAction : Action
    }
}

private class TestReducer : StateReducer<TestFeature.State, TestFeature.Message, TestFeature.Action> {
    override fun reduce(
        state: TestFeature.State,
        message: TestFeature.Message
    ): Pair<TestFeature.State, Set<TestFeature.Action>> =
        when (message) {
            TestFeature.Message.ProduceThreeActionsAndCancel -> {
                state to setOf(
                    TestFeature.Action.Action1,
                    TestFeature.Action.Action2,
                    TestFeature.Action.Action3,
                    TestFeature.Action.CancelFeatureAction
                )
            }
            TestFeature.Message.ProduceThreeActionsWithCancellationInTheMiddle -> {
                state to setOf(
                    TestFeature.Action.Action1,
                    TestFeature.Action.CancelFeatureAction,
                    TestFeature.Action.Action2,
                    TestFeature.Action.Action3
                )
            }
        }
}