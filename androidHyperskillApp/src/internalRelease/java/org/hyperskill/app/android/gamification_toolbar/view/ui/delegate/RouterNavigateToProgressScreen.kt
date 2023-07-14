package org.hyperskill.app.android.gamification_toolbar.view.ui.delegate

import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.progress.navigation.ProgressScreen

fun Router.navigateToProgressScreen() {
    navigateTo(ProgressScreen)
}