package org.hyperskill.app.android.profile_settings.view.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentProfileSettingsBinding

class ProfileSettingsDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "ProfileSettingsDialogFragment"

        const val LIGHT = 0
        const val DARK = 1
        const val SYSTEM = 2

        const val TERMS_OF_SERVICE_URL = "https://www.jetbrains.com/legal/terms/jetbrains-academy.html"
        const val PRIVACY_POLICY_URL = "https://hi.hyperskill.org/terms"
        const val HELP_CENTER_URL = "https://support.hyperskill.org/hc/en-us"

        fun newInstance(): DialogFragment =
            ProfileSettingsDialogFragment()
    }

    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding: FragmentProfileSettingsBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentProfileSettingsBinding.inflate(LayoutInflater.from(context))

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.settingsCenteredToolbar) {
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

        binding.settingsThemeButton.setOnClickListener {
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

        binding.settingsTermsOfServiceButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(TERMS_OF_SERVICE_URL)
            ContextCompat.startActivity(requireContext(), intent, null)
        }

        binding.settingsPrivacyPolicyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(PRIVACY_POLICY_URL)
            ContextCompat.startActivity(requireContext(), intent, null)
        }

        binding.settingsHelpCenterButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(HELP_CENTER_URL)
            ContextCompat.startActivity(requireContext(), intent, null)
        }

        binding.settingsVersionTextView.text = HyperskillApp.graph().commonComponent.userAgentInfo.versionName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}