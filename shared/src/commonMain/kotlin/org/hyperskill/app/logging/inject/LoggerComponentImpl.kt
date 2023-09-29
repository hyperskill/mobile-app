package org.hyperskill.app.logging.inject

import co.touchlab.kermit.Logger
import org.hyperskill.app.core.injection.AppGraph

class LoggerComponentImpl(appGraph: AppGraph) : LoggerComponent {

    override val logger: Logger = LoggerBuilder.build(
        appGraph.commonComponent.buildKonfig.buildVariant,
        appGraph.sentryComponent.sentryInteractor
    )
}