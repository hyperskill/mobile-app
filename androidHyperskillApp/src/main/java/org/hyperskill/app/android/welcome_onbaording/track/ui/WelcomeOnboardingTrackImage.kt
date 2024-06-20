package org.hyperskill.app.android.welcome_onbaording.track.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.hyperskill.app.android.R
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

val WelcomeOnboardingTrack.imagePainter: Painter
    @Composable
    get() {
        val res = when (this) {
            WelcomeOnboardingTrack.JAVA -> R.drawable.img_onboarding_choose_track_java
            WelcomeOnboardingTrack.JAVA_SCRIPT -> R.drawable.img_onboarding_choose_track_js
            WelcomeOnboardingTrack.KOTLIN -> R.drawable.img_onboarding_choose_track_kotlin
            WelcomeOnboardingTrack.PYTHON -> R.drawable.img_onboarding_choose_track_python
            WelcomeOnboardingTrack.SQL -> R.drawable.img_onboarding_choose_track_sql
        }
        return painterResource(id = res)
    }