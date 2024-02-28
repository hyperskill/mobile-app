package org.hyperskill.app.users_questionnaire.onboarding.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingViewModel

class PlatformUsersQuestionnaireOnboardingComponentImpl(
    private val usersQuestionnaireOnboardingComponent: UsersQuestionnaireOnboardingComponent
) : PlatformUsersQuestionnaireOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                UsersQuestionnaireOnboardingViewModel::class.java to {
                    UsersQuestionnaireOnboardingViewModel(
                        usersQuestionnaireOnboardingComponent.usersQuestionnaireOnboardingFeature.wrapWithFlowView()
                    )
                }
            )
        )
}