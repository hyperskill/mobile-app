package org.hyperskill.app.android.profile_settings.view.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentProfileSettingsBinding

class ProfileSettingsDialogFragment : DialogFragment(R.layout.fragment_profile_settings) {
    companion object {
        const val TAG = "ProfileSettingsDialogFragment"

        const val LIGHT = 0
        const val DARK = 1
        const val SYSTEM = 2

        fun newInstance(): DialogFragment =
            ProfileSettingsDialogFragment()
    }

    private val viewBinding: FragmentProfileSettingsBinding by viewBinding(FragmentProfileSettingsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding.settingsCenteredToolbar) {
            centeredToolbarTitle.setText(R.string.settings_title)
            centeredToolbarTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Body2)
            centeredToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

            centeredToolbar.setNavigationOnClickListener { dismiss() }
            centeredToolbar.setNavigationIcon(R.drawable.ic_close_thin)
        }

        val themes = arrayOf(
            resources.getString(R.string.settings_theme_light),
            resources.getString(R.string.settings_theme_dark),
            resources.getString(R.string.settings_theme_system)
        )

        viewBinding.settingsThemeButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(R.string.settings_theme)
                .setSingleChoiceItems(themes, -1) { _, which ->
                    val mode = when (which) {
                        LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                        DARK -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }

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
    }

    private fun openLinkInBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        ContextCompat.startActivity(requireContext(), intent, null)
    }
}