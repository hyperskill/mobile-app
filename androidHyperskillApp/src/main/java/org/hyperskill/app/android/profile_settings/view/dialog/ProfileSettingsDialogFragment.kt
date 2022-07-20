package org.hyperskill.app.android.profile_settings.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentProfileSettingsBinding

class ProfileSettingsDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "ProfileSettingsDialogFragment"

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}