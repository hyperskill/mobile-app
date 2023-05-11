package org.hyperskill.app.profile.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile.domain.analytic.ProfileClickedDailyStudyRemindsTimeHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedDailyStudyRemindsToggleHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedSettingsHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedViewFullProfileHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileViewedHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeAnalyticState
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeCardAnalyticAction
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeClickedCardActionHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeClickedModalActionButtonHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeModalAnalyticAction
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeModalShownHyperskillAnalyticEvent
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class ProfileReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    if (message.isInitCurrent) {
                        State.Loading to setOf(Action.FetchCurrentProfile)
                    } else {
                        State.Loading to setOf(Action.FetchProfile(message.profileId!!))
                    }
                } else {
                    null
                }
            }
            is Message.ProfileFetchResult.Success ->
                State.Content(
                    message.profile,
                    message.streak,
                    message.streakFreezeState,
                    message.dailyStudyRemindersState
                ) to emptySet()
            is Message.ProfileFetchResult.Error ->
                State.Error to emptySet()
            is Message.PullToRefresh ->
                if (state is State.Content && !state.isRefreshing) {
                    state.copy(isRefreshing = true) to setOf(
                        if (message.isRefreshCurrent) {
                            Action.FetchCurrentProfile
                        } else {
                            Action.FetchProfile(message.profileId!!)
                        },
                        Action.LogAnalyticEvent(ProfileClickedPullToRefreshHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            is Message.StepQuizSolved -> {
                if (state is State.Content) {
                    state.copy(streak = state.streak?.getStreakWithTodaySolved()) to emptySet()
                } else {
                    null
                }
            }
            is Message.HypercoinsBalanceChanged -> {
                if (state is State.Content) {
                    state.copy(
                        profile = state.profile.copy(
                            gamification = state.profile.gamification.copy(
                                hypercoinsBalance = message.hypercoinsBalance
                            )
                        ),
                        streakFreezeState = if (
                            state.streakFreezeState is ProfileFeature.StreakFreezeState.NotEnoughGems &&
                            state.streakFreezeState.price <= message.hypercoinsBalance
                        ) {
                            ProfileFeature.StreakFreezeState.CanBuy(
                                state.streakFreezeState.streakFreezeProductId,
                                state.streakFreezeState.price
                            )
                        } else if (
                            state.streakFreezeState is ProfileFeature.StreakFreezeState.CanBuy &&
                            state.streakFreezeState.price > message.hypercoinsBalance
                        ) {
                            ProfileFeature.StreakFreezeState.NotEnoughGems(
                                state.streakFreezeState.streakFreezeProductId,
                                state.streakFreezeState.price
                            )
                        } else {
                            state.streakFreezeState
                        }
                    ) to emptySet()
                } else {
                    null
                }
            }
            is Message.StreakChanged -> {
                if (state is State.Content) {
                    state.copy(streak = message.streak) to emptySet()
                } else {
                    null
                }
            }
            is Message.ClickedViewFullProfile -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = true) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.Profile(state.profile.id)),
                        Action.LogAnalyticEvent(ProfileClickedViewFullProfileHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveSuccess -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.OpenUrl(message.url))
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveFailure ->
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowGetMagicLinkError)
                } else {
                    null
                }
            is Message.StreakFreezeCardButtonClicked ->
                if (state is State.Content && state.streakFreezeState != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StreakFreezeClickedCardActionHyperskillAnalyticEvent(
                                when (state.streakFreezeState) {
                                    ProfileFeature.StreakFreezeState.AlreadyHave ->
                                        StreakFreezeCardAnalyticAction.STREAK_FREEZE_ICON
                                    is ProfileFeature.StreakFreezeState.CanBuy,
                                    is ProfileFeature.StreakFreezeState.NotEnoughGems ->
                                        StreakFreezeCardAnalyticAction.GET_STREAK_FREEZE
                                }
                            )
                        ),
                        Action.ViewAction.ShowStreakFreezeModal(state.streakFreezeState)
                    )
                } else {
                    null
                }
            is Message.StreakFreezeModalButtonClicked ->
                if (state is State.Content && state.streakFreezeState != null) {
                    state to when (state.streakFreezeState) {
                        is ProfileFeature.StreakFreezeState.CanBuy ->
                            setOf(
                                Action.BuyStreakFreeze(
                                    streakFreezeProductId = state.streakFreezeState.streakFreezeProductId,
                                    streakFreezePrice = state.streakFreezeState.price
                                ),
                                Action.LogAnalyticEvent(
                                    StreakFreezeClickedModalActionButtonHyperskillAnalyticEvent(
                                        StreakFreezeModalAnalyticAction.GET_IT,
                                        mapContentStateToStreakFreezeAnalyticState(state.streakFreezeState)
                                    )
                                ),
                                Action.ViewAction.ShowStreakFreezeBuyingStatus.Loading
                            )
                        ProfileFeature.StreakFreezeState.AlreadyHave,
                        is ProfileFeature.StreakFreezeState.NotEnoughGems ->
                            setOf(
                                Action.LogAnalyticEvent(
                                    StreakFreezeClickedModalActionButtonHyperskillAnalyticEvent(
                                        StreakFreezeModalAnalyticAction.CONTINUE_LEARNING,
                                        mapContentStateToStreakFreezeAnalyticState(state.streakFreezeState)
                                    )
                                ),
                                Action.ViewAction.HideStreakFreezeModal,
                                Action.ViewAction.NavigateTo.HomeScreen
                            )
                    }
                } else {
                    null
                }
            is Message.BuyStreakFreezeResult.Success ->
                if (state is State.Content) {
                    state.copy(streakFreezeState = ProfileFeature.StreakFreezeState.AlreadyHave) to setOf(
                        Action.ViewAction.ShowStreakFreezeBuyingStatus.Success,
                        Action.ViewAction.HideStreakFreezeModal
                    )
                } else {
                    null
                }
            is Message.BuyStreakFreezeResult.Error ->
                if (state is State.Content) {
                    state to setOf(Action.ViewAction.ShowStreakFreezeBuyingStatus.Error)
                } else {
                    null
                }
            is Message.DailyStudyRemindersToggleClicked ->
                if (state is State.Content) {
                    state.copy(
                        dailyStudyRemindersState = state.dailyStudyRemindersState.copy(isEnabled = message.isEnabled)
                    ) to setOf(
                        Action.SaveDailyStudyRemindersIsEnabled(message.isEnabled),
                        Action.LogAnalyticEvent(
                            ProfileClickedDailyStudyRemindsToggleHyperskillAnalyticEvent(message.isEnabled)
                        )
                    )
                } else {
                    null
                }
            is Message.DailyStudyRemindersIsEnabledChanged ->
                if (state is State.Content) {
                    state.copy(
                        dailyStudyRemindersState = state.dailyStudyRemindersState.copy(isEnabled = message.isEnabled)
                    ) to emptySet()
                } else {
                    null
                }
            is Message.DailyStudyRemindersIntervalStartHourChanged ->
                if (state is State.Content && state.dailyStudyRemindersState.isEnabled) {
                    state.copy(
                        dailyStudyRemindersState = state.dailyStudyRemindersState.copy(
                            startHour = message.startHour
                        )
                    ) to setOf(
                        Action.SaveDailyStudyRemindersIntervalStartHour(message.startHour)
                    )
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileViewedHyperskillAnalyticEvent()))
            is Message.ClickedSettingsEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileClickedSettingsHyperskillAnalyticEvent()))
            is Message.ClickedDailyStudyRemindsTimeEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(ProfileClickedDailyStudyRemindsTimeHyperskillAnalyticEvent())
                )
            is Message.StreakFreezeModalShownEventMessage ->
                if (state is State.Content && state.streakFreezeState != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StreakFreezeModalShownHyperskillAnalyticEvent(
                                mapContentStateToStreakFreezeAnalyticState(state.streakFreezeState)
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.StreakFreezeModalHiddenEventMessage ->
                state to setOf(Action.LogAnalyticEvent(StreakFreezeModalHiddenHyperskillAnalyticEvent()))
        } ?: (state to emptySet())

    private fun mapContentStateToStreakFreezeAnalyticState(
        streakFreezeState: ProfileFeature.StreakFreezeState
    ): StreakFreezeAnalyticState =
        when (streakFreezeState) {
            ProfileFeature.StreakFreezeState.AlreadyHave -> StreakFreezeAnalyticState.ALREADY_HAVE
            is ProfileFeature.StreakFreezeState.CanBuy -> StreakFreezeAnalyticState.CAN_BUY
            is ProfileFeature.StreakFreezeState.NotEnoughGems -> StreakFreezeAnalyticState.NOT_ENOUGH_GEMS
        }
}