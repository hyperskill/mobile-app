package org.hyperskill.app.logging.inject

import co.touchlab.kermit.Logger
import org.hyperskill.app.core.injection.AppGraph

class LoggerComponentImpl(private val appGraph: AppGraph) : LoggerComponent {
    override val logger: Logger
        get() = LoggerBuilder.build(
            appGraph.commonComponent.buildKonfig.buildVariant,
            appGraph.sentryComponent.sentryInteractor
        )
}