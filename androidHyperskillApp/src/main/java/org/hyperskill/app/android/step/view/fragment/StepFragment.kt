package org.hyperskill.app.android.step.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireMainRouter
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepBinding
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.step.view.dialog.TopicPracticeCompletedBottomSheet
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.step_practice.view.fragment.StepPracticeFragment
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.step_theory.view.fragment.StepTheoryFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StepFragment :
    Fragment(R.layout.fragment_step),
    ReduxView<StepFeature.State, StepFeature.Action.ViewAction>,
    StepCompletionHost {

    companion object {
        private const val STEP_TAG = "step"

        fun newInstance(stepRoute: StepRoute): Fragment =
            StepFragment()
                .apply {
                    this.stepRoute = stepRoute
                }
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentStepBinding by viewBinding(FragmentStepBinding::bind)
    private val stepViewModel: StepViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<StepFeature.State> = ViewStateDelegate()

    private val platformNotificationComponent =
        HyperskillApp.graph().platformNotificationComponent

    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        viewBinding.stepError.tryAgain.setOnClickListener {
            stepViewModel.onNewMessage(StepFeature.Message.Initialize(forceUpdate = true))
        }
        stepViewModel.onNewMessage(StepFeature.Message.Initialize())
        stepViewModel.onNewMessage(StepFeature.Message.ViewedEventMessage)
    }

    private fun injectComponent() {
        val stepComponent = HyperskillApp.graph().buildStepComponent(stepRoute)
        val platformStepComponent = HyperskillApp.graph().buildPlatformStepComponent(stepComponent)
        viewModelFactory = platformStepComponent.reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        with(viewStateDelegate) {
            addState<StepFeature.State.Idle>()
            addState<StepFeature.State.Loading>(viewBinding.stepProgress) // TODO Replace progress bar with skeletons
            addState<StepFeature.State.Error>(viewBinding.stepError.root)
            addState<StepFeature.State.Data>(viewBinding.stepContainer)
        }
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        when (action) {
            is StepFeature.Action.ViewAction.StepCompletionViewAction -> {
                when (val stepCompletionAction = action.viewAction) {
                    StepCompletionFeature.Action.ViewAction.NavigateTo.Back -> {
                        requireRouter().exit()
                    }

                    StepCompletionFeature.Action.ViewAction.NavigateTo.HomeScreen -> {
                        requireRouter().backTo(MainScreen)
                        parentFragmentManager.requireMainRouter().switch(HomeScreen)
                    }

                    is StepCompletionFeature.Action.ViewAction.ReloadStep -> {
                        requireRouter().replaceScreen(StepScreen(stepCompletionAction.stepRoute))
                    }

                    is StepCompletionFeature.Action.ViewAction.ShowStartPracticingError -> {
                        view?.snackbar(stepCompletionAction.message)
                    }

                    is StepCompletionFeature.Action.ViewAction.ShowTopicCompletedModal -> {
                        TopicPracticeCompletedBottomSheet
                            .newInstance(
                                stepCompletionAction.modalText,
                                stepCompletionAction.isNextStepAvailable
                            )
                            .showIfNotExists(
                                childFragmentManager,
                                TopicPracticeCompletedBottomSheet.Tag
                            )
                    }
                    StepCompletionFeature.Action.ViewAction.RequestDailyStudyRemindersPermission -> {
                        requestSendDailyStudyRemindersPermission()
                    }
                    is StepCompletionFeature.Action.ViewAction.ShowProblemOfDaySolvedModal -> {
                        CompletedStepOfTheDayDialogFragment
                            .newInstance(earnedGemsText = stepCompletionAction.earnedGemsText)
                            .showIfNotExists(childFragmentManager, CompletedStepOfTheDayDialogFragment.TAG)
                    }
                }
            }
        }
    }

    override fun render(state: StepFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is StepFeature.State.Data) {
            initStepContainer(state)
            (childFragmentManager.findFragmentByTag(STEP_TAG) as? StepCompletionView)
                ?.render(state.stepCompletionState.isPracticingLoading)
        }
    }

    override fun onNewMessage(message: StepCompletionFeature.Message) {
        stepViewModel.onNewMessage(
            StepFeature.Message.StepCompletionMessage(message)
        )
    }

    private fun initStepContainer(data: StepFeature.State.Data) {
        setChildFragment(R.id.stepContainer, STEP_TAG) {
            if (data.step.type == Step.Type.PRACTICE) {
                StepPracticeFragment.newInstance(data.step, stepRoute)
            } else {
                StepTheoryFragment.newInstance(data.step, stepRoute, data.isPracticingAvailable)
            }
        }
    }

    private fun requestSendDailyStudyRemindersPermission() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(SharedResources.strings.after_daily_step_completed_dialog_title.resourceId)
            .setMessage(SharedResources.strings.after_daily_step_completed_dialog_text.resourceId)
            .setPositiveButton(org.hyperskill.app.R.string.ok) { dialog, _ ->
                onSendDailyStudyReminderAccepted()
                dialog.dismiss()
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { dialog, _ ->
                stepViewModel.onNewMessage(
                    StepFeature.Message.StepCompletionMessage(
                        StepCompletionFeature.Message.RequestDailyStudyRemindersPermissionResult(
                            isGranted = false
                        )
                    )
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun onSendDailyStudyReminderAccepted() {
        stepViewModel.onNewMessage(
            StepFeature.Message.StepCompletionMessage(
                StepCompletionFeature.Message.RequestDailyStudyRemindersPermissionResult(
                    isGranted = true
                )
            )
        )
        NotificationManagerCompat.from(requireContext()).checkNotificationChannelAvailability(
            requireContext(),
            HyperskillNotificationChannel.DailyReminder
        ) {
            if (isResumed) {
                viewBinding.root.snackbar(org.hyperskill.app.R.string.common_error)
            }
        }
        platformNotificationComponent.dailyStudyReminderNotificationDelegate.scheduleDailyNotification()
    }
}