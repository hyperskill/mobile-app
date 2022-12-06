package org.hyperskill.app.android.auth.view.ui.navigation

import org.hyperskill.app.profile.domain.model.Profile

interface AuthFlow {
    fun onAuthSuccess(profile: Profile)
}