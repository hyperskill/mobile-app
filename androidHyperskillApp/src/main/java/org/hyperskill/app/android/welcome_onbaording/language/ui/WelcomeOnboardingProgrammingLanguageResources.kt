package org.hyperskill.app.android.welcome_onbaording.language.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.hyperskill.app.android.R
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.R as SharedR

val WelcomeOnboardingProgrammingLanguage.title: String
    @Composable
    get() {
        val res = when (this) {
            WelcomeOnboardingProgrammingLanguage.JAVA -> SharedR.string.welcome_onboarding_lang_java
            WelcomeOnboardingProgrammingLanguage.JAVA_SCRIPT -> SharedR.string.welcome_onboarding_lang_js
            WelcomeOnboardingProgrammingLanguage.KOTLIN -> SharedR.string.welcome_onboarding_lang_kotlin
            WelcomeOnboardingProgrammingLanguage.PYTHON -> SharedR.string.welcome_onboarding_lang_python
            WelcomeOnboardingProgrammingLanguage.SQL -> SharedR.string.welcome_onboarding_lang_sql
            WelcomeOnboardingProgrammingLanguage.UNDEFINED -> SharedR.string.welcome_onboarding_lang_not_sure
        }
        return stringResource(id = res)
    }

val WelcomeOnboardingProgrammingLanguage.iconPainter: Painter
    @Composable
    get() {
        val res = when (this) {
            WelcomeOnboardingProgrammingLanguage.JAVA -> R.drawable.ic_welcome_onboarding_lang_java
            WelcomeOnboardingProgrammingLanguage.JAVA_SCRIPT -> R.drawable.ic_welcome_onboarding_lang_js
            WelcomeOnboardingProgrammingLanguage.KOTLIN -> R.drawable.ic_welcome_onboarding_lang_kotlin
            WelcomeOnboardingProgrammingLanguage.PYTHON -> R.drawable.ic_welcome_onboarding_lang_python
            WelcomeOnboardingProgrammingLanguage.SQL -> R.drawable.ic_welcome_onboarding_lang_sql
            WelcomeOnboardingProgrammingLanguage.UNDEFINED -> R.drawable.ic_welcome_questionnaire_other
        }
        return painterResource(id = res)
    }