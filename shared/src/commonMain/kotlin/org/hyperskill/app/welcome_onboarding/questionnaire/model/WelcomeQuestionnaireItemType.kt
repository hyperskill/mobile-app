package org.hyperskill.app.welcome_onboarding.questionnaire.model

sealed interface WelcomeQuestionnaireItemType {
    enum class ClientSource : WelcomeQuestionnaireItemType {
        TIK_TOK,
        GOOGLE_SEARCH,
        NEWS,
        APP_STORE,
        FACEBOOK,
        FRIENDS,
        YOUTUBE,
        OTHER
    }

    enum class LearningGoal : WelcomeQuestionnaireItemType {
        START_CAREER,
        CURRENT_JOB,
        CHANGE_STACK,
        STUDIES,
        FUN,
        OTHER
    }

    enum class CodingBackground : WelcomeQuestionnaireItemType {
        NO_CODING_EXPERIENCE,
        BASIC_UNDERSTANDING,
        WRITTEN_SOME_PROJECTS,
        WORKING_PROFESSIONALLY
    }
}