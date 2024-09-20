package org.hyperskill.app.run_code.injection

import org.hyperskill.app.run_code.domain.repository.RunCodeRepository

interface RunCodeDataComponent {
    val runCodeRepository: RunCodeRepository
}