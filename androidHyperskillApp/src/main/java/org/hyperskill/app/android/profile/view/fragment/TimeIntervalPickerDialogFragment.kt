package org.hyperskill.app.android.profile.view.fragment

import android.app.Dialog
import android.content.DialogInterface
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

        fun newInstance(selectedHour: Int): TimeIntervalPickerDialogFragment =
            TimeIntervalPickerDialogFragment().apply {
                this.selectedHour = selectedHour
            }

    }

    private var selectedHour: Int by argument()

    private var picker: NumberPicker? = null

    private val callback: Callback
        get() = parentFragment as Callback

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val picker = NumberPicker(context)
        this.picker = picker
        setupPicked(picker)
        return getDialog(picker).apply {
            setOnShowListener {
                if (savedInstanceState == null) {
                    callback.onTimeIntervalDialogShown()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        picker = null
    }

    override fun onCancel(dialog: DialogInterface) {
        callback.onTimeIntervalDialogHidden()
    }

    private fun getDialog(picker: NumberPicker): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(org.hyperskill.app.R.string.choose_notification_time_interval)
            .setView(picker)
            .setPositiveButton(org.hyperskill.app.R.string.ok) { _, _ ->
                this.picker?.value?.let(callback::onTimeIntervalPicked)
            }
            .setNegativeButton(org.hyperskill.app.R.string.cancel, null)
            .create()

    private fun setupPicked(picker: NumberPicker) {
        with(picker) {
            minValue = 0
            maxValue = TimeIntervalUtil.values.size - 1
            displayedValues = TimeIntervalUtil.values
            value = selectedHour
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            setBackgroundColor(0x0)
            textColor = requireContext().resolveColorAttribute(com.google.android.material.R.attr.colorOnSurface)
            selectedTextColor =
                requireContext().resolveColorAttribute(com.google.android.material.R.attr.colorOnSurface)
            dividerColor = 0x0

            try {
                textSize = 50f // Warning: reflection!
            } catch (exception: Exception) {
            }
        }
    }

    interface Callback {
        fun onTimeIntervalDialogShown() {}

        fun onTimeIntervalDialogHidden() {}
        fun onTimeIntervalPicked(chosenInterval: Int)
    }
}
