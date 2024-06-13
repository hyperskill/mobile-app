package org.hyperskill.app.welcome_onboarding.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.view.WelcomeQuestionnaireItemType.ClientSource
import org.hyperskill.app.welcome_onboarding.view.WelcomeQuestionnaireItemType.CodingBackground
import org.hyperskill.app.welcome_onboarding.view.WelcomeQuestionnaireItemType.LearningGoal

class WelcomeQuestionnaireViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val platformType: PlatformType
) {
    fun mapQuestionnaireTypeToViewState(type: WelcomeQuestionnaireType): WelcomeQuestionnaireViewState =
        when (type) {
            WelcomeQuestionnaireType.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL ->
                getHowDidYouHearAboutHyperskillViewState()
            WelcomeQuestionnaireType.LEARNING_REASON -> getLearningReasonViewState()
            WelcomeQuestionnaireType.CODING_EXPERIENCE -> getCodingBackgroundViewState()
        }

    private fun getHowDidYouHearAboutHyperskillViewState(): WelcomeQuestionnaireViewState =
        WelcomeQuestionnaireViewState(
            title = resourceProvider.getString(
                SharedResources.strings.welcome_questionnaire_how_did_you_hear_about_hyperskill_title
            ),
            items =  listOf(
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    resourceProvider.getString(SharedResources.strings.welcome_questionnaire_tiktok_item)
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.GOOGLE_SEARCH,
                    resourceProvider.getString(SharedResources.strings.welcome_questionnaire_google_item)
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.NEWS,
                    resourceProvider.getString(SharedResources.strings.welcome_questionnaire_news_item)
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.APP_STORE,
                    resourceProvider.getString(
                        when (platformType) {
                            PlatformType.IOS ->
                                SharedResources.strings.welcome_questionnaire_app_store_item
                            PlatformType.ANDROID ->
                                SharedResources.strings.welcome_questionnaire_play_store_item
                        }
                    )
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.FACEBOOK,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_facebook_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.FRIENDS,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_friends_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.YOUTUBE,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_youtube_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.OTHER,
                    resourceProvider.getString(SharedResources.strings.welcome_questionnaire_other_item)
                )
            )
        )

    private fun getLearningReasonViewState(): WelcomeQuestionnaireViewState =
        WelcomeQuestionnaireViewState(
            title = resourceProvider.getString(SharedResources.strings.welcome_questionnaire_learning_goal_title),
            items = listOf(
                WelcomeQuestionnaireItem(
                    LearningGoal.START_CAREER,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_learning_goal_start_career_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    LearningGoal.CURRENT_JOB,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_learning_goal_current_job_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    LearningGoal.CHANGE_STACK,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_learning_goal_change_stack_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    LearningGoal.STUDIES,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_learning_goal_study_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    LearningGoal.FUN,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_learning_goal_fun_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    LearningGoal.OTHER,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_learning_goal_different_reason_item
                    )
                )
            )
        )

    private fun getCodingBackgroundViewState(): WelcomeQuestionnaireViewState =
        WelcomeQuestionnaireViewState(
            title = resourceProvider.getString(SharedResources.strings.welcome_questionnaire_background_title),
            items = listOf(
                WelcomeQuestionnaireItem(
                    CodingBackground.NO_CODING_EXPERIENCE,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_background_no_experience_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    CodingBackground.BASIC_UNDERSTANDING,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_background_basic_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    CodingBackground.WRITTEN_SOME_PROJECTS,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_background_medium_item
                    )
                ),
                WelcomeQuestionnaireItem(
                    CodingBackground.WORKING_PROFESSIONALLY,
                    resourceProvider.getString(
                        SharedResources.strings.welcome_questionnaire_background_pro_item
                    )
                )
            )
        )
}