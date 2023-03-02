package org.hyperskill.app.android.profile.view.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shawnlin.numberpicker.NumberPicker
import org.hyperskill.app.android.view.base.ui.extension.TimeIntervalUtil
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute

class TimeIntervalPickerDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "time_interval_picker_dialog"

        fun newInstance(intervalStartHour: Int): TimeIntervalPickerDialogFragment =
            TimeIntervalPickerDialogFragment()
                .apply {
                    this.intervalStartHour = intervalStartHour
                }

        interface Callback {
            fun onTimeIntervalPicked(chosenInterval: Int)
        }
    }

    private lateinit var picker: NumberPicker

    private lateinit var callback: Callback

    private var intervalStartHour: Int by argument()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        picker = NumberPicker(context)
        picker.minValue = 0
        picker.maxValue = TimeIntervalUtil.values.size - 1
        picker.displayedValues = TimeIntervalUtil.values
        picker.value = intervalStartHour
        picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        picker.wrapSelectorWheel = false
        picker.setBackgroundColor(0x0)
        picker.textColor = requireContext().resolveColorAttribute(com.google.android.material.R.attr.colorOnSurface)
        picker.selectedTextColor = requireContext().resolveColorAttribute(com.google.android.material.R.attr.colorOnSurface)
        picker.dividerColor = 0x0

        try {
            picker.textSize = 50f // Warning: reflection!
        } catch (_: Exception) {}

        callback = parentFragment as Callback

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(org.hyperskill.app.R.string.choose_notification_time_interval)
            .setView(picker)
            .setPositiveButton(org.hyperskill.app.R.string.ok) { _, _ ->
                callback.onTimeIntervalPicked(picker.value)
            }
            .setNegativeButton(org.hyperskill.app.R.string.cancel, null)
            .create()
    }
}
