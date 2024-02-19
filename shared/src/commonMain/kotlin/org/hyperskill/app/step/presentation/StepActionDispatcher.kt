package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.profile.domain.model.isMobileShortTheoryEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.InternalAction
import org.hyperskill.app.step.presentation.StepFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StepActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchStep ->
                handleFetchStepAction(action, ::onNewMessage)
            is Action.ViewStep ->
                stepInteractor.viewStep(action.stepId, action.stepContext)
            is Action.UpdateNextLearningActivityState ->
                handleUpdateNextLearningActivityStateAction(action)
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchStepAction(
        action: InternalAction.FetchStep,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStepScreenRemoteDataLoading(),
            onError = { Message.StepLoaded.Error }
        ) {
            val step = stepInteractor
                .getStep(action.stepRoute.stepId)
                .getOrThrow()
                .let { applyMobileShortTheoryFeature(it) }
            Message.StepLoaded.Success(step)
        }.let(onNewMessage)
    }

    private suspend fun applyMobileShortTheoryFeature(step: Step): Step {
        val isMobileShortTheoryEnabled = currentProfileStateRepository
            .getState(forceUpdate = false)
            .getOrNull()
            ?.features
            ?.isMobileShortTheoryEnabled
            ?: false

        return if (isMobileShortTheoryEnabled && step.id == 38627L) {
            step.copy(
                block = step.block.copy(
                    text = """
            <p>Ever wondered why Java's logo is a steaming cup of coffee? Just as coffee fuels our day, Java powers the tech world with its robust and versatile features! So, grab your cup of coffee and join us on this exciting journey into the world of Java!</p>
            <h5 id="what-is-java">What is Java</h5>
            <p><span style="font-size: inherit; font-weight: inherit;">Java language was designed by </span>James Gosling in 1995<span style="font-size: inherit; font-weight: inherit;"> to be simple </span><span style="font-size: inherit; font-weight: inherit;">and powerful</span><span style="font-size: inherit; font-weight: inherit;">. </span>It borrows syntax from C and C++, and complements it with <strong>automatic memory management</strong>, and other powerful features. Java's core principle is<strong> </strong>"<strong>Write Once, Run Anywhere</strong>" (WORA), which means that any Java program is platform-independent and can run on any operating system without modifications. </p>
            <h5 id="where-is-java-applied">Where is Java Applied</h5>
            <p>Waking up you immediately interact with an application built with Java — your phone alarm. When you work or develop your pet projects, Java forms the backbone of development tools like <u>IntelliJ IDEA</u>. Even when relaxing with <u>Netflix</u>, <u>Spotify,</u> or <u>Minecraft</u>, you rely on Java power. Java is like a silent friend, aiding us and making our lives easier in numerous ways, from the moment we wake up till we call it a day. What amazing and helpful Java application are you going to create?</p>
            <h5 id="a-sample-of-java">A sample of Java</h5>
            <p>Let's start with the classic "Hello, World!" program, a friendly greeting from your computer:</p>
            <pre><code class="language-java">public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("Hello, World!");
                }
            }</code></pre>
            <p>This program simply prints the phrase "Hello, World!" to the console. Don't worry if it looks a bit cryptic now. We'll dive deeper into its logic during the practice part of this topic.</p>
            <h5 id="conclusion">Conclusion</h5>
            <p>Java is a high-level object-oriented programming language. Its clear syntax, platform independence, and automatic memory management contribute to its popularity. Become a part of the vast Java community by practicing the language basics now. D<span style="font-size: inherit; font-weight: inherit;">on't hesitate to experiment, ask for help if you're stuck, and </span>embrace mistakes as they fuel your learning!<span style="font-size: inherit; font-weight: inherit;"> </span></p>
        """.trimIndent()
                )
            )
        } else {
            step
        }
    }

    private suspend fun handleUpdateNextLearningActivityStateAction(
        action: Action.UpdateNextLearningActivityState
    ) {
        val currentNextLearningActivityState = nextLearningActivityStateRepository
            .getStateWithSource(forceUpdate = false)
            .getOrElse { return }

        if (currentNextLearningActivityState.usedDataSourceType == DataSourceType.REMOTE) {
            return
        }

        val isInTheSameTopic = currentNextLearningActivityState.state?.topicId == action.step.topic
        val isStepNotCurrent = currentNextLearningActivityState.state?.targetId != action.step.id

        if (isInTheSameTopic && isStepNotCurrent) {
            nextLearningActivityStateRepository.reloadState()
        }
    }
}