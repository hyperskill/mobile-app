package org.hyperskill.app.android.welcome_onbaording.questionnaire.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.hyperskill.app.android.R
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType.ClientSource
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType.CodingBackground
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType.LearningGoal

val WelcomeQuestionnaireItemType.iconPainter: Painter
    @Composable
    get() {
        val res: Int = when (this) {
            ClientSource.TIK_TOK -> R.drawable.ic_welcome_questionnaire_tiktok
            ClientSource.GOOGLE_SEARCH -> R.drawable.ic_welcome_questionnaire_google
            ClientSource.NEWS -> R.drawable.ic_welcome_questionnaire_news
            ClientSource.APP_STORE -> R.drawable.ic_welcome_questionnaire_play_store
            ClientSource.FACEBOOK -> R.drawable.ic_welcome_questionnaire_facebook
            ClientSource.FRIENDS -> R.drawable.ic_welcome_questionnaire_friends
            ClientSource.YOUTUBE -> R.drawable.ic_welcome_questionnaire_youtube
            ClientSource.OTHER -> R.drawable.ic_welcome_questionnaire_other
            LearningGoal.START_CAREER -> R.drawable.ic_welcome_questionnaire_start_career
            LearningGoal.CURRENT_JOB -> R.drawable.ic_welcome_questionnaire_current_job
            LearningGoal.CHANGE_STACK -> R.drawable.ic_welcome_questionnaire_change_stack
            LearningGoal.STUDIES -> R.drawable.ic_welcome_questionnaire_studies
            LearningGoal.FUN -> R.drawable.ic_welcome_questionnaire_fun
            LearningGoal.OTHER -> R.drawable.ic_welcome_questionnaire_other_learning_goal
            CodingBackground.NO_CODING_EXPERIENCE -> R.drawable.ic_welcome_questionnaire_no_experience
            CodingBackground.BASIC_UNDERSTANDING -> R.drawable.ic_welcome_questionnaire_basic_experience
            CodingBackground.WRITTEN_SOME_PROJECTS -> R.drawable.ic_welcome_questionnaire_medium_experience
            CodingBackground.WORKING_PROFESSIONALLY -> R.drawable.ic_welcome_questionnaire_pro
        }
        return painterResource(id = res)
    }