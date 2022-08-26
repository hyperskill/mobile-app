package org.hyperskill.app.android.profile_settings.view.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.representation
import org.hyperskill.app.android.databinding.FragmentProfileSettingsBinding
import org.hyperskill.app.android.profile_settings.view.mapper.ThemeMapper
import org.hyperskill.app.profile.presentation.ProfileSettingsViewModel
import org.hyperskill.app.profile_settings.domain.model.Theme
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProfileSettingsDialogFragment :
    DialogFragment(R.layout.fragment_profile_settings),
    ReduxView<ProfileSettingsFeature.State, ProfileSettingsFeature.Action.ViewAction> {
    companion object {
        const val TAG = "ProfileSettingsDialogFragment"

        fun newInstance(): DialogFragment =
            ProfileSettingsDialogFragment()
    }

    private val viewBinding: FragmentProfileSettingsBinding by viewBinding(FragmentProfileSettingsBinding::bind)

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val profileSettingsViewModel: ProfileSettingsViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<ProfileSettingsFeature.State> = ViewStateDelegate()

    private var currentThemePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        injectComponents()
    }

    private fun injectComponents() {
        val profileSettingsComponent = HyperskillApp.graph().buildProfileSettingsComponent()
        val platformProfileSettingsComponent = HyperskillApp.graph().buildPlatformProfileSettingsComponent(profileSettingsComponent)
        viewModelFactory = platformProfileSettingsComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding.settingsCenteredToolbar) {
            centeredToolbarTitle.setText(R.string.settings_title)
            centeredToolbarTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Body2)
            centeredToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

            centeredToolbar.setNavigationOnClickListener {
                profileSettingsViewModel.onNewMessage(ProfileSettingsFeature.Message.ProfileSettingsClickedDoneEventMessage)
                dismiss()
            }
            centeredToolbar.setNavigationIcon(R.drawable.ic_close_thin)
        }

        viewBinding.settingsThemeButton.setOnClickListener {
            profileSettingsViewModel.onNewMessage(ProfileSettingsFeature.Message.ProfileSettingsClickedThemeEventMessage)

            MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(R.string.settings_theme)
                .setSingleChoiceItems(Theme.values().map { theme -> theme.representation }.toTypedArray(), currentThemePosition) { _, which ->
                    val newTheme = Theme.values()[which]

                    profileSettingsViewModel.onNewMessage(ProfileSettingsFeature.Message.ThemeChanged(theme = newTheme))
                    viewBinding.settingsThemeChosenTextView.text = newTheme.representation

                    val mode = ThemeMapper.getAppCompatDelegate(newTheme)
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        viewBinding.settingsTermsOfServiceButton.setOnClickListener {
            openLinkInBrowser(resources.getString(R.string.settings_terms_of_service_url))
        }

        viewBinding.settingsPrivacyPolicyButton.setOnClickListener {
            openLinkInBrowser(resources.getString(R.string.settings_privacy_policy_url))
        }

        viewBinding.settingsHelpCenterButton.setOnClickListener {
            openLinkInBrowser(resources.getString(R.string.settings_help_center_url))
        }

        viewBinding.settingsVersionTextView.text = HyperskillApp.graph().commonComponent.userAgentInfo.versionName

        viewBinding.settingsLogoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog_LogoutDialog)
                .setTitle(R.string.settings_logout_dialog_title)
                .setMessage(R.string.settings_logout_dialog_explanation)
                .setPositiveButton(R.string.yes) { _, _ ->
                    profileSettingsViewModel.onNewMessage(ProfileSettingsFeature.Message.LogoutConfirmed)
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        profileSettingsViewModel.onNewMessage(ProfileSettingsFeature.Message.InitMessage())
        profileSettingsViewModel.onNewMessage(ProfileSettingsFeature.Message.ProfileSettingsViewedEventMessage)
    }

    private fun openLinkInBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        ContextCompat.startActivity(requireContext(), intent, null)
    }

    override fun onAction(action: ProfileSettingsFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: ProfileSettingsFeature.State) {
        viewStateDelegate.switchState(state)

        if (state is ProfileSettingsFeature.State.Content) {
            viewBinding.settingsThemeChosenTextView.text = state.profileSettings.theme.representation
            currentThemePosition = state.profileSettings.theme.ordinal
        }
    }
}