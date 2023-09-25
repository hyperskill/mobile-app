package org.hyperskill.app.logging.inject

import co.touchlab.kermit.Logger

interface LoggerComponent {
    val logger: Logger
}