package org.hyperskill.app.android.profile_settings.view.dialog

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.getStringRepresentation
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentProfileSettingsBinding
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.android.profile_settings.view.mapper.asNightMode
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.profile.presentation.ProfileSettingsViewModel
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailData
import org.hyperskill.app.profile_settings.domain.model.Theme
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.ViewState
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProfileSettingsDialogFragment :
    DialogFragment(R.layout.fragment_profile_settings),
    ReduxView<ViewState, Action.ViewAction> {
    companion object {
        const val TAG = "ProfileSettingsDialogFragment"

        fun newInstance(): DialogFragment =
            ProfileSettingsDialogFragment()
    }

    private val viewBinding: FragmentProfileSettingsBinding by viewBinding(FragmentProfileSettingsBinding::bind)

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val profileSettingsViewModel: ProfileSettingsViewModel by reduxViewModel(this) { viewModelFactory }
    private var viewStateDelegate: ViewStateDelegate<ViewState>? = null

    private var currentThemePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        injectComponents()
    }

    private fun injectComponents() {
        val profileSettingsComponent = HyperskillApp.graph().buildProfileSettingsComponent()
        val platformProfileSettingsComponent =
            HyperskillApp.graph().buildPlatformProfileSettingsComponent(profileSettingsComponent)
        viewModelFactory = platformProfileSettingsComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewStateDelegate(viewBinding)

        with(viewBinding.settingsCenteredToolbar) {
            centeredToolbarTitle.setText(org.hyperskill.app.R.string.settings_title)
            centeredToolbarTitle.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body2)
            centeredToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

            centeredToolbar.setNavigationOnClickListener {
                profileSettingsViewModel.onNewMessage(Message.ClickedDoneEventMessage)
                dismiss()
            }
            centeredToolbar.setNavigationIcon(R.drawable.ic_close_thin)
        }
        viewBinding.settingsContent.settingsThemeButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedThemeEventMessage)
            MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(org.hyperskill.app.R.string.settings_theme)
                .setSingleChoiceItems(
                    Theme.values().map { theme -> theme.getStringRepresentation(requireContext()) }.toTypedArray(),
                    currentThemePosition
                ) { _, which ->
                    val newTheme = Theme.values()[which]

                    profileSettingsViewModel.onNewMessage(Message.ThemeChanged(theme = newTheme))
                    viewBinding.settingsContent.settingsThemeChosenTextView.text =
                        newTheme.getStringRepresentation(requireContext())
                    AppCompatDelegate.setDefaultNightMode(newTheme.asNightMode())
                }
                .setNegativeButton(org.hyperskill.app.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        viewBinding.settingsContent.settingsTermsOfServiceButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedTermsOfServiceEventMessage)
            openLinkInBrowser(resources.getString(org.hyperskill.app.R.string.settings_terms_of_service_url))
        }

        viewBinding.settingsContent.settingsPrivacyPolicyButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedPrivacyPolicyEventMessage)
            openLinkInBrowser(resources.getString(org.hyperskill.app.R.string.settings_privacy_policy_url))
        }

        viewBinding.settingsContent.settingsReportProblemButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedReportProblemEventMessage)
            openLinkInBrowser(resources.getString(org.hyperskill.app.R.string.settings_report_problem_url))
        }

        viewBinding.settingsContent.settingsSendFeedbackButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedSendFeedback)
        }

        viewBinding.settingsContent.settingsSubscriptionFrameLayout.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.SubscriptionDetailsClicked)
        }

        val userAgentInfo = HyperskillApp.graph().commonComponent.userAgentInfo
        @SuppressLint("SetTextI18n")
        viewBinding.settingsContent.settingsVersionTextView.text =
            "${userAgentInfo.versionName} (${userAgentInfo.versionCode})"

        viewBinding.settingsContent.settingsLogoutButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedSignOutEventMessage)

            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.ThemeOverlay_App_MaterialAlertDialog_ProfileSettingsConfirmDialog
            )
                .setTitle(org.hyperskill.app.R.string.settings_sign_out_dialog_title)
                .setMessage(org.hyperskill.app.R.string.settings_sign_out_dialog_explanation)
                .setPositiveButton(org.hyperskill.app.R.string.yes) { _, _ ->
                    profileSettingsViewModel.onNewMessage(
                        Message.SignOutNoticeHiddenEventMessage(
                            isConfirmed = true
                        )
                    )
                    profileSettingsViewModel.onNewMessage(Message.SignOutConfirmed)
                }
                .setNegativeButton(org.hyperskill.app.R.string.no) { dialog, _ ->
                    profileSettingsViewModel.onNewMessage(
                        Message.SignOutNoticeHiddenEventMessage(
                            isConfirmed = false
                        )
                    )
                    dialog.dismiss()
                }
                .show()

            profileSettingsViewModel.onNewMessage(Message.SignOutNoticeShownEventMessage)
        }

        viewBinding.settingsContent.settingsDeleteAccountButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(Message.ClickedDeleteAccountEventMessage)

            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.ThemeOverlay_App_MaterialAlertDialog_ProfileSettingsConfirmDialog
            )
                .setTitle(org.hyperskill.app.R.string.settings_account_deletion_dialog_title)
                .setMessage(org.hyperskill.app.R.string.settings_account_deletion_dialog_explanation)
                .setPositiveButton(
                    org.hyperskill.app.R.string.settings_account_deletion_dialog_delete_button_text
                ) { _, _ ->
                    profileSettingsViewModel.onNewMessage(
                        Message.DeleteAccountNoticeHidden(true)
                    )
                }
                .setNegativeButton(org.hyperskill.app.R.string.cancel) { dialog, _ ->
                    profileSettingsViewModel.onNewMessage(
                        Message.DeleteAccountNoticeHidden(false)
                    )
                    dialog.dismiss()
                }
                .show()

            profileSettingsViewModel.onNewMessage(Message.DeleteAccountNoticeShownEventMessage)
        }

        profileSettingsViewModel.onNewMessage(Message.InitMessage())
        profileSettingsViewModel.onNewMessage(Message.ViewedEventMessage)
    }

    private fun initViewStateDelegate(viewBinding: FragmentProfileSettingsBinding) {
        viewStateDelegate = ViewStateDelegate<ViewState>().apply {
            addState<ViewState.Idle>()
            addState<ViewState.Loading>(viewBinding.settingsProgress)
            addState<ViewState.Content>(viewBinding.settingsContent.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    private fun openLinkInBrowser(link: String) {
        requireContext().openUrl(link)
    }

    override fun onAction(action: Action.ViewAction) {
        when (action) {
            is Action.ViewAction.SendFeedback ->
                sendEmailFeedback(action.feedbackEmailData)
            is Action.ViewAction.OpenUrl ->
                openLinkInBrowser(action.url)
            is Action.ViewAction.ShowGetMagicLinkError ->
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
            is Action.ViewAction.NavigateTo.Paywall -> {
                requireRouter()
                    .navigateTo(PaywallScreen(action.paywallTransitionSource))
            }
            is Action.ViewAction.NavigateTo.SubscriptionManagement -> {
                // TODO
            }
            else -> {
                // no op
            }
        }
    }

    override fun render(state: ViewState) {
        viewStateDelegate?.switchState(state)

        if (state is ViewState.Content) {
            if (state.isLoadingMagicLink) {
                LoadingProgressDialogFragment.newInstance()
                    .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
            } else {
                childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
            viewBinding.settingsContent.settingsThemeChosenTextView.text =
                state.profileSettings.theme.getStringRepresentation(requireContext())
            currentThemePosition = state.profileSettings.theme.ordinal
            renderSubscription(state)
        }
    }

    private fun renderSubscription(
        state: ViewState.Content
    ) {
        state.subscriptionState?.description
            ?.let(viewBinding.settingsContent.settingsSubscriptionHeader::setText)
        viewBinding.settingsContent.settingsSubscriptionLinearLayout.isVisible =
            state.subscriptionState != null
    }

    private fun sendEmailFeedback(feedbackEmailData: FeedbackEmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
            .setData(Uri.parse("mailto:"))
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(feedbackEmailData.mailTo))
            .putExtra(Intent.EXTRA_SUBJECT, feedbackEmailData.subject)
            .putExtra(Intent.EXTRA_TEXT, feedbackEmailData.body)
        try {
            startActivity(Intent.createChooser(intent, "Select your E-Mail app"))
        } catch (e: ActivityNotFoundException) {
            viewBinding.root.snackbar(org.hyperskill.app.R.string.common_error)
        }
    }
}