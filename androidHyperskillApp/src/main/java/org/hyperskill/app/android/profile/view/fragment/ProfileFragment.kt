package org.hyperskill.app.android.profile.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import co.touchlab.kermit.Logger
import coil.ImageLoader
import coil.load
import coil.result
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.badges.view.delegate.ProfileBadgesDelegate
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.extensions.doOnApplyWindowInsets
import org.hyperskill.app.android.core.extensions.isChannelNotificationsEnabled
import org.hyperskill.app.android.core.extensions.logger
import org.hyperskill.app.android.core.extensions.startAppNotificationSettingsIntent
import org.hyperskill.app.android.core.view.ui.setHyperskillColors
import org.hyperskill.app.android.core.view.ui.updateIsRefreshing
import org.hyperskill.app.android.databinding.FragmentProfileBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.permission.NotificationPermissionDelegate
import org.hyperskill.app.android.profile.view.delegate.AboutMeDelegate
import org.hyperskill.app.android.profile.view.delegate.ProfileLoadingDelegate
import org.hyperskill.app.android.profile.view.delegate.ProfileViewActionDelegate
import org.hyperskill.app.android.profile.view.delegate.StreakCardFormDelegate
import org.hyperskill.app.android.profile.view.dialog.BadgeDetailsDialogFragment
import org.hyperskill.app.android.profile_settings.view.dialog.ProfileSettingsDialogFragment
import org.hyperskill.app.android.view.base.ui.extension.setElevationOnCollapsed
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.presentation.ProfileViewModel
import org.hyperskill.app.profile.view.BadgesViewStateMapper
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProfileFragment :
    Fragment(R.layout.fragment_profile),
    ReduxView<ProfileFeature.State, ProfileFeature.Action.ViewAction>,
    TimeIntervalPickerDialogFragment.Callback,
    BadgeDetailsDialogFragment.Callback {
    companion object {
        private const val LOG_TAG = "ProfileFragment"

        fun newInstance(profileId: Long? = null, isInitCurrent: Boolean = true): Fragment =
            ProfileFragment()
                .apply {
                    this.profileId = profileId ?: 0
                    this.isInitCurrent = isInitCurrent
                }
    }

    private var profileId: Long by argument()
    private var isInitCurrent: Boolean by argument()

    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val profileViewModel: ProfileViewModel by reduxViewModel(this) { viewModelFactory }
    private var viewStateDelegate: ViewStateDelegate<ProfileFeature.State>? = null
    private val profileBadgesDelegate: ProfileBadgesDelegate by lazy(LazyThreadSafetyMode.NONE) {
        ProfileBadgesDelegate(
            BadgesViewStateMapper(
                HyperskillApp.graph().commonComponent.resourceProvider
            )
        )
    }

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    private val notificationManager: NotificationManagerCompat by lazy(LazyThreadSafetyMode.NONE) {
        NotificationManagerCompat.from(requireContext())
    }

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    private val logger: Logger by logger(LOG_TAG)

    private var notificationPermissionDelegate: NotificationPermissionDelegate? = null

    private var profileLoadingDelegate: ProfileLoadingDelegate? = null

    private val dailyReminderCheckChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                onDailyLearningSwitchNotificationPermissionGranted(false)
            } else {
                notificationPermissionDelegate?.requestNotificationPermission()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
        notificationPermissionDelegate = NotificationPermissionDelegate(this, ::onNotificationPermissionResult)
    }

    private fun injectComponents() {
        val profileComponent = HyperskillApp.graph().buildProfileComponent()
        val platformProfileComponent = HyperskillApp.graph().buildPlatformProfileComponent(profileComponent)
        viewModelFactory = platformProfileComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyWindowInsets()
        initViewStateDelegate()
        initToolbar()
        AboutMeDelegate.setup(viewBinding.profileAboutMeLayout, profileViewModel::onNewMessage)
        StreakCardFormDelegate.setup(
            context = requireContext(),
            binding = viewBinding.profileStreakLayout,
            onFreezeButtonClick = {
                profileViewModel.onNewMessage(ProfileFeature.Message.StreakFreezeCardButtonClicked)
            }
        )
        profileBadgesDelegate.setup(
            activity = requireActivity(),
            composeView = viewBinding.profileBadges,
            onBadgeClick = { badgeKind ->
                profileViewModel.onNewMessage(ProfileFeature.Message.BadgeClicked(badgeKind))
            },
            onExpandButtonClick = { button ->
                profileViewModel.onNewMessage(
                    ProfileFeature.Message.BadgesVisibilityButtonClicked(button)
                )
            }
        )

        viewBinding.profileError.tryAgain.setOnClickListener {
            profileViewModel.onNewMessage(
                ProfileFeature.Message.Initialize(
                    profileId = profileId,
                    forceUpdate = true,
                    isInitCurrent = isInitCurrent
                )
            )
        }

        with(viewBinding.profileSwipeRefreshLayout) {
            setHyperskillColors()
            setOnRefreshListener {
                profileViewModel.onNewMessage(
                    ProfileFeature.Message.PullToRefresh(
                        isRefreshCurrent = isInitCurrent,
                        profileId = profileId
                    )
                )
            }
        }

        profileLoadingDelegate = ProfileLoadingDelegate(childFragmentManager, viewLifecycleOwner.lifecycle)

        profileViewModel.onNewMessage(
            ProfileFeature.Message.Initialize(
                profileId = profileId,
                isInitCurrent = isInitCurrent
            )
        )
        profileViewModel.onNewMessage(ProfileFeature.Message.ViewedEventMessage)
    }

    private fun applyWindowInsets() {
        viewBinding.root.doOnApplyWindowInsets { _, insets, _ ->
            val insetTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            val toolbar = viewBinding.profileToolbar
            toolbar.updateLayoutParams<CollapsingToolbarLayout.LayoutParams> {
                height += insetTop
            }
            toolbar.updatePaddingRelative(top = insetTop)

            val collapsingToolbarLayout = viewBinding.profileCollapsingToolbarLayout

            collapsingToolbarLayout.updateLayoutParams<AppBarLayout.LayoutParams> {
                height += insetTop
            }
            collapsingToolbarLayout.expandedTitleMarginTop = insetTop
        }
    }

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<ProfileFeature.State>().apply {
            addState<ProfileFeature.State.Idle>()
            addState<ProfileFeature.State.Loading>(
                viewBinding.profileAppBar,
                viewBinding.profileSkeleton.root
            )
            addState<ProfileFeature.State.Error>(
                viewBinding.profileAppBar,
                viewBinding.profileError.root
            )
            addState<ProfileFeature.State.Content>(
                viewBinding.profileAppBar,
                viewBinding.profileContentNestedScrollView,
                viewBinding.profileSettingsButton
            )
        }
    }

    private fun onNotificationPermissionResult(
        result: NotificationPermissionDelegate.Result
    ) {
        when (result) {
            NotificationPermissionDelegate.Result.GRANTED ->
                onDailyLearningSwitchNotificationPermissionGranted(notificationSwitchChecked = true)
            NotificationPermissionDelegate.Result.DENIED -> {
                viewBinding.profileDailyReminder.profileDailyRemindersSwitchCompat.isChecked = false
                view?.snackbar(R.string.daily_reminder_notification_permission_explanation)
            }
            NotificationPermissionDelegate.Result.DONT_ASK -> {
                viewBinding.profileDailyReminder.profileDailyRemindersSwitchCompat.isChecked = false
                view?.snackbar(
                    getString(R.string.daily_reminder_notification_permission_explanation_with_settings)
                ) {
                    setAction(R.string.daily_reminder_notification_settings_button) {
                        if (isResumed) {
                            this@ProfileFragment.context?.startAppNotificationSettingsIntent {
                                this@ProfileFragment.view?.snackbar(org.hyperskill.app.R.string.common_error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onDailyLearningSwitchNotificationPermissionGranted(notificationSwitchChecked: Boolean) {
        profileViewModel.onNewMessage(
            ProfileFeature.Message.DailyStudyRemindersToggleClicked(notificationSwitchChecked)
        )

        if (notificationSwitchChecked) {
            val isEnabled = notificationManager.checkNotificationChannelAvailability(
                requireContext(),
                HyperskillNotificationChannel.DailyReminder
            )
            viewBinding.profileDailyReminder.profileDailyRemindersSwitchCompat.isChecked = isEnabled
            viewBinding.profileDailyReminder.profileScheduleTextView.isVisible = isEnabled
        } else {
            viewBinding.profileDailyReminder.profileScheduleTextView.isVisible = false
        }
    }

    private fun initToolbar() {
        with(viewBinding.profileAppBar) {
            setElevationOnCollapsed(viewLifecycleOwner.lifecycle)
            setExpanded(true)
        }
        viewBinding.profileSettingsButton.setOnClickListener {
            profileViewModel.onNewMessage(ProfileFeature.Message.ClickedSettingsEventMessage)
            ProfileSettingsDialogFragment
                .newInstance()
                .showIfNotExists(childFragmentManager, ProfileSettingsDialogFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
        profileLoadingDelegate = null
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationPermissionDelegate = null
    }

    override fun onTimeIntervalPicked(chosenInterval: Int) {
        profileViewModel.onNewMessage(
            ProfileFeature.Message.DailyStudyRemindersIntervalStartHourChanged(chosenInterval)
        )
    }

    override fun onAction(action: ProfileFeature.Action.ViewAction) {
        ProfileViewActionDelegate.onAction(
            fragment = this,
            mainScreenRouter = mainScreenRouter,
            logger = logger,
            action = action
        )
    }

    override fun onBadgeDetailsDialogFragmentShown(badgeKind: BadgeKind) {
        profileViewModel.onNewMessage(ProfileFeature.Message.BadgeModalShownEventMessage(badgeKind))
    }

    override fun onBadgeDetailsDialogFragmentHidden(badgeKind: BadgeKind) {
        profileViewModel.onNewMessage(ProfileFeature.Message.BadgeModalHiddenEventMessage(badgeKind))
    }

    override fun render(state: ProfileFeature.State) {
        viewStateDelegate?.switchState(state)
        renderSwipeRefresh(state)
        when (state) {
            is ProfileFeature.State.Content -> {
                profileId = state.profile.id
                renderContent(state)
            }
            else -> {
                // no op
            }
        }
    }

    private fun renderContent(content: ProfileFeature.State.Content) {
        StreakCardFormDelegate.render(
            context = requireContext(),
            binding = viewBinding.profileStreakLayout,
            streak = content.streak,
            freezeState = content.streakFreezeState
        )

        renderStatistics(content.profile)
        renderNameHeader(content.profile)
        renderReminderSchedule(content.dailyStudyRemindersState, notificationManager)
        AboutMeDelegate.render(requireContext(), viewBinding.profileAboutMeLayout, content.profile, logger)
        profileBadgesDelegate.render(content.badgesState)

        profileLoadingDelegate?.render(content.isLoadingShowed)
    }

    private fun renderSwipeRefresh(content: ProfileFeature.State) {
        with(viewBinding.profileSwipeRefreshLayout) {
            isEnabled = content is ProfileFeature.State.Content
            if (content is ProfileFeature.State.Content) {
                updateIsRefreshing(content.isRefreshing)
            }
        }
    }

    private fun renderNameHeader(profile: Profile) {
        with(viewBinding.profileHeader) {
            profileAvatarImageView.load(profile.avatar, imageLoader) {
                transformations(CircleCropTransformation())
            }
            profileAvatarImageView.result?.request?.memoryCacheKey
            profileNameTextView.setTextIfChanged(profile.fullname)
            profileRoleTextView.setTextIfChanged(
                if (profile.isStaff) {
                    resources.getString(org.hyperskill.app.R.string.profile_role_staff_text)
                } else {
                    resources.getString(org.hyperskill.app.R.string.profile_role_learner_text)
                }
            )
        }
    }

    private fun renderReminderSchedule(
        remindersState: ProfileFeature.DailyStudyRemindersState,
        notificationManager: NotificationManagerCompat
    ) {
        with(viewBinding.profileDailyReminder) {
            profileScheduleTextView.text = getScheduleTimeText(
                time = remindersState.startHour
            )
            profileScheduleTextView.setOnClickListener {
                onDailyStudyRemindersScheduleClick(remindersState.startHour)
            }

            val isDailyNotificationEnabled = notificationManager.isChannelNotificationsEnabled(
                HyperskillNotificationChannel.DailyReminder.channelId
            ) && remindersState.isEnabled
            with(profileDailyRemindersSwitchCompat) {
                setOnCheckedChangeListener(null)
                this.isChecked = isDailyNotificationEnabled
                setOnCheckedChangeListener(dailyReminderCheckChangeListener)
            }
            profileScheduleTextView.isVisible = isDailyNotificationEnabled
        }
    }

    private fun onDailyStudyRemindersScheduleClick(notificationHour: Int) {
        profileViewModel.onNewMessage(ProfileFeature.Message.ClickedDailyStudyRemindsTimeEventMessage)
        TimeIntervalPickerDialogFragment
            .newInstance(notificationHour)
            .showIfNotExists(childFragmentManager, TimeIntervalPickerDialogFragment.TAG)
    }

    private fun renderStatistics(profile: Profile) {
        with(viewBinding.profileStatisticsLayout) {
            profileGemsCountTextView.setTextIfChanged(profile.gamification.hypercoinsBalance.toString())
            profileTracksCountTextView.setTextIfChanged(profile.completedTracks.size.toString())
            profileProjectsCountTextView.setTextIfChanged(profile.gamification.passedProjectsCount.toString())
        }
    }

    private fun getScheduleTimeText(time: Int) =
        requireContext().resources.getString(org.hyperskill.app.R.string.profile_daily_study_reminders_schedule_text) +
            "${time.toString().padStart(2, '0')}:00 - ${(time + 1).toString().padStart(2, '0')}:00"
}