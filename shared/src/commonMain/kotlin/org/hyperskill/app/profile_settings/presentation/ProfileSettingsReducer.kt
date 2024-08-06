package org.hyperskill.app.profile_settings.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsClickedHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsDeleteAccountNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsSignOutNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsSignOutNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsViewedHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class ProfileSettingsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitMessage -> {
                if (state is State.Idle) {
                    State.Loading to setOf(Action.FetchProfileSettings)
                } else {
                    null
                }
            }
            is Message.ProfileSettingsSuccess ->
                State.Content(
                    profileSettings = message.profileSettings,
                    subscription = message.subscription,
                    mobileOnlyFormattedPrice = message.mobileOnlyFormattedPrice
                ) to emptySet()
            is Message.OnSubscriptionChanged ->
                handleSubscriptionChanged(state, message)
            is Message.ThemeChanged ->
                if (state is State.Content) {
                    state.copy(state.profileSettings.copy(theme = message.theme)) to
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
                            target = HyperskillAnalyticTarget.DONE,
                            part = HyperskillAnalyticPart.HEAD
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
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsSignOutNoticeHiddenHyperskillAnalyticEvent(message.isConfirmed)
                    )
                )
            is Message.ClickedDeleteAccountEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.DELETE_ACCOUNT
                        )
                    )
                )
            is Message.DeleteAccountNoticeShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(ProfileSettingsDeleteAccountNoticeShownHyperskillAnalyticEvent())
                )
            is Message.DeleteAccountNoticeHidden -> {
                val logAnalyticEventAction = Action.LogAnalyticEvent(
                    ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent(
                        message.isConfirmed
                    )
                )
                if (message.isConfirmed && state is State.Content) {
                    state.copy(isLoadingMagicLink = true) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.DeleteAccount()),
                        logAnalyticEventAction
                    )
                } else {
                    state to setOf(logAnalyticEventAction)
                }
            }
            Message.ClickedRateUsInAppStoreEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.RATE_US_IN_APP_STORE
                        )
                    )
                )
            Message.ClickedRateUsInPlayStoreEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.RATE_US_IN_PLAY_STORE
                        )
                    )
                )
            is Message.GetMagicLinkReceiveSuccess -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.OpenUrl(message.url))
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveFailure -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowGetMagicLinkError)
                } else {
                    null
                }
            }
            is Message.SubscriptionDetailsClicked ->
                handleSubscriptionDetailsClicked(state)
        } ?: (state to emptySet())

    private fun handleSubscriptionChanged(
        state: State,
        message: Message.OnSubscriptionChanged
    ): ReducerResult =
        if (state is State.Content) {
            state.copy(subscription = message.subscription) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleSubscriptionDetailsClicked(
        state: State
    ): ReducerResult =
        if (state is State.Content) {
            state to when {
                state.subscription?.type == SubscriptionType.MOBILE_ONLY ->
                    setOf(
                        Action.LogAnalyticEvent(
                            ProfileSettingsClickedHyperskillAnalyticEvent(
                                HyperskillAnalyticTarget.ACTIVE_SUBSCRIPTION_DETAILS
                            )
                        ),
                        Action.ViewAction.NavigateTo.SubscriptionManagement
                    )
                state.subscription?.type?.canUpgradeToMobileOnly == true ->
                    setOf(
                        Action.LogAnalyticEvent(
                            ProfileSettingsClickedHyperskillAnalyticEvent(
                                HyperskillAnalyticTarget.SUBSCRIPTION_SUGGESTION_DETAILS
                            )
                        ),
                        Action.ViewAction.NavigateTo.Paywall(
                            PaywallTransitionSource.PROFILE_SETTINGS
                        )
                    )
                else -> emptySet()
            }
        } else {
            state to emptySet()
        }
}