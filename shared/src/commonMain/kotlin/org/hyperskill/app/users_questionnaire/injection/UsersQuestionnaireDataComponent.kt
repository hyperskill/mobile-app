package org.hyperskill.app.users_questionnaire.injection

import org.hyperskill.app.users_questionnaire.domain.repository.UsersQuestionnaireRepository

interface UsersQuestionnaireDataComponent {
    val usersQuestionnaireRepository: UsersQuestionnaireRepository
}