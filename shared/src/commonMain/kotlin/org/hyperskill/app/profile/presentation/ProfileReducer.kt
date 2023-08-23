package org.hyperskill.app.profile.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile.domain.analytic.ProfileClickedDailyStudyRemindsTimeHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedDailyStudyRemindsToggleHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedSettingsHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileClickedViewFullProfileHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.ProfileViewedHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.badges.ProfileBadgeModalHiddenHyperskillAnalyticsEvent
import org.hyperskill.app.profile.domain.analytic.badges.ProfileBadgeModalShownHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.badges.ProfileClickedBadgeCardHyperskillAnalyticsEvent
import org.hyperskill.app.profile.domain.analytic.badges.ProfileClickedBadgesVisibilityButtonHyperskillAnalyticsEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeAnalyticState
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeCardAnalyticAction
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeClickedCardActionHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeClickedModalActionButtonHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeModalAnalyticAction
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.analytic.streak_freeze.StreakFreezeModalShownHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class ProfileReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
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
            is Message.ProfileFetchResult ->
                handleProfileFetchResult(message)
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
            is Message.ProfileChanged ->
                handleProfileChanged(state, message)
            is Message.StreakChanged -> {
                if (state is State.Content) {
                    state.copy(streak = message.streak) to emptySet()
                } else {
                    null
                }
            }
            is Message.ClickedViewFullProfile -> {
                if (state is State.Content) {
                    state.copy(isLoadingShowed = true) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.Profile(state.profile.id)),
                        Action.LogAnalyticEvent(ProfileClickedViewFullProfileHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveSuccess -> {
                if (state is State.Content) {
                    state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.OpenUrl(message.url))
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveFailure ->
                if (state is State.Content) {
                    state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.ShowError.MagicLink)
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
                handleDailyStudyRemindersToggleClicked(state, message)
            is Message.DailyStudyRemindersIsEnabledUpdateResult ->
                handleDailyStudyReminderIsEnabledUpdateResult(state, message)
            is Message.DailyStudyRemindersIntervalStartHourChanged ->
                handleDailyStudyRemindersIntervalStartHourChanged(state, message)
            is Message.DailyStudyRemindersIntervalStartHourSaveResult ->
                handleDailyStudyRemindersIntervalStartHourSaveResult(state, message)
            is Message.DailyStudyRemindersIsEnabledChanged ->
                if (state is State.Content) {
                    state.copy(
                        dailyStudyRemindersState = state.dailyStudyRemindersState.copy(isEnabled = message.isEnabled)
                    ) to emptySet()
                } else {
                    null
                }
            is Message.BadgesVisibilityButtonClicked -> handleBadgesVisibilityButtonClicked(state, message)
            is Message.BadgeClicked -> handleBadgeClicked(state, message)
            is Message.BadgeModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(ProfileBadgeModalShownHyperskillAnalyticEvent(message.badgeKind))
                )
            is Message.BadgeModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileBadgeModalHiddenHyperskillAnalyticsEvent(message.badgeKind)
                    )
                )
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

    private fun handleProfileFetchResult(
        message: Message.ProfileFetchResult
    ): ReducerResult =
        when (message) {
            is Message.ProfileFetchResult.Success ->
                State.Content(
                    profile = message.profile,
                    streak = message.streak,
                    streakFreezeState = message.streakFreezeState,
                    dailyStudyRemindersState = getDailyStudyRemindersState(
                        profile = message.profile,
                        defaultDailyStudyReminderHour = message.defaultDailyStudyReminderHour
                    ),
                    badgesState = ProfileFeature.BadgesState(
                        badges = message.badges,
                        isExpanded = false
                    )
                ) to emptySet()
            Message.ProfileFetchResult.Error ->
                State.Error to emptySet()
        }

    private fun handleProfileChanged(
        state: State,
        message: Message.ProfileChanged
    ): ReducerResult =
        state.updateContent { content ->
            content.copy(
                profile = message.profile,
                streakFreezeState = if (
                    content.streakFreezeState is ProfileFeature.StreakFreezeState.NotEnoughGems &&
                    content.streakFreezeState.price <= message.profile.gamification.hypercoinsBalance
                ) {
                    ProfileFeature.StreakFreezeState.CanBuy(
                        content.streakFreezeState.streakFreezeProductId,
                        content.streakFreezeState.price
                    )
                } else if (
                    content.streakFreezeState is ProfileFeature.StreakFreezeState.CanBuy &&
                    content.streakFreezeState.price > message.profile.gamification.hypercoinsBalance
                ) {
                    ProfileFeature.StreakFreezeState.NotEnoughGems(
                        content.streakFreezeState.streakFreezeProductId,
                        content.streakFreezeState.price
                    )
                } else {
                    content.streakFreezeState
                },
                dailyStudyRemindersState = getDailyStudyRemindersState(
                    profile = message.profile,
                    defaultDailyStudyReminderHour = message.defaultDailyStudyReminderHour
                )
            ) to emptySet()
        }

    private fun getDailyStudyRemindersState(
        profile: Profile,
        defaultDailyStudyReminderHour: Int
    ): ProfileFeature.DailyStudyRemindersState =
        ProfileFeature.DailyStudyRemindersState(
            isEnabled = profile.isDailyLearningNotificationEnabled,
            startHour = profile.dailyLearningNotificationHour ?: defaultDailyStudyReminderHour
        )

    private fun handleBadgesVisibilityButtonClicked(
        state: State,
        message: Message.BadgesVisibilityButtonClicked
    ): ReducerResult =
        if (state is State.Content) {
            val badgesState = state.badgesState
            state.copy(
                badgesState = badgesState.copy(
                    isExpanded = message.visibilityButton == Message.BadgesVisibilityButton.SHOW_ALL
                )
            ) to setOf(
                Action.LogAnalyticEvent(
                    ProfileClickedBadgesVisibilityButtonHyperskillAnalyticsEvent(message.visibilityButton)
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleBadgeClicked(
        state: State,
        message: Message.BadgeClicked
    ): ReducerResult =
        if (state is State.Content) {
            val clickedBadge = state.badgesState.badges.firstOrNull { it.kind == message.badgeKind }
            val showAction = Action.ViewAction.ShowBadgeDetailsModal(
                if (clickedBadge == null) {
                    Action.ViewAction.BadgeDetails.BadgeKind(message.badgeKind)
                } else {
                    Action.ViewAction.BadgeDetails.FullBadge(clickedBadge)
                }
            )
            state to setOf(
                showAction,
                Action.LogAnalyticEvent(
                    ProfileClickedBadgeCardHyperskillAnalyticsEvent(
                        badgeKind = message.badgeKind,
                        isLocked = message.badgeKind !in state.badgesState.badges.map { it.kind }
                    )
                )
            )
        } else {
            state to emptySet()
        }

    private fun handleDailyStudyRemindersToggleClicked(
        state: State,
        message: Message.DailyStudyRemindersToggleClicked
    ): ReducerResult =
        state.updateContent { content ->
            content.copy(isLoadingShowed = true) to
                setOf(
                    Action.SaveDailyStudyRemindersIsEnabled(message.isEnabled),
                    Action.LogAnalyticEvent(
                        ProfileClickedDailyStudyRemindsToggleHyperskillAnalyticEvent(message.isEnabled)
                    )
                )
        }

    private fun handleDailyStudyReminderIsEnabledUpdateResult(
        state: State,
        message: Message.DailyStudyRemindersIsEnabledUpdateResult
    ): ReducerResult =
        state.updateContent { content ->
            content.copy(
                isLoadingShowed = false,
                dailyStudyRemindersState = when (message) {
                    is Message.DailyStudyRemindersIsEnabledUpdateResult.Success ->
                        content.dailyStudyRemindersState.copy(isEnabled = message.isEnabled)
                    Message.DailyStudyRemindersIsEnabledUpdateResult.Error ->
                        content.dailyStudyRemindersState
                }
            ) to if (message is Message.DailyStudyRemindersIsEnabledUpdateResult.Error) {
                setOf(Action.ViewAction.ShowError.DailyStudyReminders)
            } else {
                emptySet()
            }
        }

    private fun handleDailyStudyRemindersIntervalStartHourChanged(
        state: State,
        message: Message.DailyStudyRemindersIntervalStartHourChanged
    ): ReducerResult =
        state.updateContent { content ->
            content.copy(isLoadingShowed = true) to
                setOf(Action.SaveDailyStudyRemindersIntervalStartHour(message.startHour))
        }

    private fun handleDailyStudyRemindersIntervalStartHourSaveResult(
        state: State,
        message: Message.DailyStudyRemindersIntervalStartHourSaveResult
    ): ReducerResult =
        state.updateContent { content ->
            content.copy(
                isLoadingShowed = false,
                dailyStudyRemindersState = when (message) {
                    is Message.DailyStudyRemindersIntervalStartHourSaveResult.Success ->
                        content.dailyStudyRemindersState.copy(startHour = message.startHour)
                    Message.DailyStudyRemindersIntervalStartHourSaveResult.Error ->
                        content.dailyStudyRemindersState
                }
            ) to if (message is Message.DailyStudyRemindersIntervalStartHourSaveResult.Error) {
                setOf(Action.ViewAction.ShowError.DailyStudyReminders)
            } else {
                emptySet()
            }
        }

    private fun State.updateContent(block: (content: State.Content) -> ReducerResult): ReducerResult =
        if (this is State.Content) {
            block(this)
        } else {
            this to emptySet()
        }
}