package org.hyperskill.app.android.badges.view.delegate

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.hyperskill.app.android.badges.view.ui.ProfileBadges
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.view.BadgesViewStateMapper

class ProfileBadgesDelegate(
    private val viewStateMapper: BadgesViewStateMapper
) {
    private val stateFlow: MutableStateFlow<ProfileFeature.BadgesState?> = MutableStateFlow(null)

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLifecycleComposeApi::class)
    fun setup(
        activity: Activity,
        composeView: ComposeView,
        onBadgeClick: (BadgeKind) -> Unit,
        onExpandButtonClick: (ProfileFeature.Message.BadgesVisibilityButton) -> Unit
    ) {
        composeView.setContent {
            MdcTheme {
                val windowSizeClass = calculateWindowSizeClass(activity)
                val viewState by stateFlow
                    .map { state ->
                        state?.let {
                            viewStateMapper.map(state)
                        }
                    }
                    .collectAsStateWithLifecycle(null)
                viewState?.let { actualViewState ->
                    ProfileBadges(
                        viewState = actualViewState,
                        windowWidthSizeClass = windowSizeClass.widthSizeClass,
                        onBadgeClick = onBadgeClick,
                        onExpandButtonClick = onExpandButtonClick
                    )
                }
            }
        }
    }

    fun render(state: ProfileFeature.BadgesState) {
        stateFlow.value = state
    }
}