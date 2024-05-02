package org.hyperskill.app.logging.injection

import co.touchlab.kermit.Logger

interface LoggerComponent {
    val logger: Logger
}