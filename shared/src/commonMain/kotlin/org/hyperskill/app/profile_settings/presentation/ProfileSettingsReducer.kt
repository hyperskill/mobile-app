package org.hyperskill.app.profile_settings.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsClickedHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsDeleteAccountNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsSignOutNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsSignOutNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsViewedHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class ProfileSettingsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitMessage -> {
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchProfileSettings)
                } else {
                    null
                }
            }
            is Message.ProfileSettingsSuccess ->
                State.Content(message.profileSettings) to emptySet()
            is Message.ProfileSettingsError ->
                State.Error to emptySet()
            is Message.ThemeChanged ->
                if (state is State.Content) {
                    State.Content(state.profileSettings.copy(theme = message.theme)) to
                        setOf(Action.ChangeTheme(message.theme))
                } else {
                    null
                }
            is Message.SignOutConfirmed ->
                state to setOf(Action.SignOut)
            is Message.DismissScreen ->
                state to setOf(Action.ViewAction.NavigateTo.ParentScreen)
            is Message.ClickedSendFeedback -> {
                val analyticEvent = ProfileSettingsClickedHyperskillAnalyticEvent(
                    target = HyperskillAnalyticTarget.SEND_FEEDBACK
                )
                state to setOf(Action.PrepareFeedbackEmailData, Action.LogAnalyticEvent(analyticEvent))
            }
            is Message.FeedbackEmailDataPrepared ->
                state to setOf(Action.ViewAction.SendFeedback(message.feedbackEmailData))
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileSettingsViewedHyperskillAnalyticEvent()))
            is Message.ClickedDoneEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            HyperskillAnalyticPart.HEAD,
                            HyperskillAnalyticTarget.DONE
                        )
                    )
                )
            is Message.ClickedThemeEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.THEME
                        )
                    )
                )
            is Message.ClickedTermsOfServiceEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.JETBRAINS_TERMS_OF_SERVICE
                        )
                    )
                )
            is Message.ClickedPrivacyPolicyEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.HYPERSKILL_TERMS_OF_SERVICE
                        )
                    )
                )
            is Message.ClickedReportProblemEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.REPORT_PROBLEM
                        )
                    )
                )
            is Message.ClickedSignOutEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.SIGN_OUT
                        )
                    )
                )
            is Message.SignOutNoticeShownEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileSettingsSignOutNoticeShownHyperskillAnalyticEvent()))
            is Message.SignOutNoticeHiddenEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileSettingsSignOutNoticeHiddenHyperskillAnalyticEvent(message.isConfirmed)))
            is Message.ClickedDeleteAccountEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.DELETE_ACCOUNT
                        )
                    )
                )
            is Message.DeleteAccountNoticeShownEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileSettingsDeleteAccountNoticeShownHyperskillAnalyticEvent()))
            is Message.DeleteAccountNoticeHiddenEventMessage -> {
                state to buildSet {
                    if (message.isConfirmed) {
                        add(Action.GetLink(HyperskillUrlPath.DeleteAccount()))
                    }
                    add(
                        Action.LogAnalyticEvent(
                            ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent(
                                message.isConfirmed
                            )
                        )
                    )
                }
            }
            is Message.LinkReceived ->
                state to setOf(Action.ViewAction.FollowLink(message.url))
            is Message.LinkReceiveFailed -> {
                //TODO: implement error showing
                state to emptySet()
            }
        } ?: (state to emptySet())
}