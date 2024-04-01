package org.hyperskill.app.submissions.injection

import org.hyperskill.app.submissions.domain.repository.SubmissionsRepository

interface SubmissionsDataComponent {
    val submissionsRepository: SubmissionsRepository
}