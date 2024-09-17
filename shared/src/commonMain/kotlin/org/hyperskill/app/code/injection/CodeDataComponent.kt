package org.hyperskill.app.code.injection

import org.hyperskill.app.code.domain.repository.CodeRepository

interface CodeDataComponent {
    val codeRepository: CodeRepository
}