package org.hyperskill.app.android.step_quiz_table.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.serialization.builtins.ListSerializer
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.BottomSheetDialogTableColumnsSelectionBinding
import org.hyperskill.app.android.step_quiz_table.view.adapter.TableColumnMultipleSelectionItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_table.view.adapter.TableColumnSingleSelectionItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.ui.adapters.selection.MultipleChoiceSelectionHelper
import ru.nobird.android.ui.adapters.selection.SelectionHelper
import ru.nobird.android.ui.adapters.selection.SingleChoiceSelectionHelper
import ru.nobird.android.view.base.ui.extension.argument

class TableColumnSelectionBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "TableRowSelectionBottomSheetDialogFragment"

        private const val ARG_SELECTED = "selected"

        fun newInstance(
            index: Int,
            rowTitle: String,
            chosenColumns: List<TableChoiceItem>,
            isCheckBox: Boolean
        ): DialogFragment =
            TableColumnSelectionBottomSheetDialogFragment()
                .apply {
                    this.index = index
                    this.rowTitle = rowTitle
                    this.chosenColumns = chosenColumns
                    this.isCheckBox = isCheckBox
                }
    }

    private val viewBinding: BottomSheetDialogTableColumnsSelectionBinding by viewBinding(
        BottomSheetDialogTableColumnsSelectionBinding::bind
    )

    private lateinit var selectionHelper: SelectionHelper

    private var index: Int by argument()
    private var rowTitle: String by argument()
    private var chosenColumns: List<TableChoiceItem> by argument(ListSerializer(TableChoiceItem.serializer()))
    private var isCheckBox: Boolean by argument()

    private val columnsAdapter = DefaultDelegateAdapter<TableChoiceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_BottomSheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.bottom_sheet_dialog_table_columns_selection, container, false)

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selected = if (savedInstanceState != null) {
            savedInstanceState.getBooleanArray(ARG_SELECTED) as BooleanArray
        } else {
            chosenColumns.map { it.answer }.toBooleanArray()
        }

        viewBinding.tableColumnSelectionRowTitle.setText(rowTitle)
        with(viewBinding.tableColumnsRecycler) {
            adapter = columnsAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
        selectionHelper = if (isCheckBox) {
            MultipleChoiceSelectionHelper(columnsAdapter)
        } else {
            SingleChoiceSelectionHelper(columnsAdapter)
        }

        val (@StringRes description, delegate) = if (isCheckBox) {
            org.hyperskill.app.R.string.step_quiz_table_multiple_choice to
                TableColumnMultipleSelectionItemAdapterDelegate(selectionHelper, ::handleColumnSelectionClick)
        } else {
            org.hyperskill.app.R.string.step_quiz_table_single_choice to
                TableColumnSingleSelectionItemAdapterDelegate(selectionHelper, ::handleColumnSelectionClick)
        }
        viewBinding.tableColumnSelectionInformation.setText(description)
        columnsAdapter += delegate
        columnsAdapter.items = chosenColumns
        mapSelection(selected)
        // This is necessary to avoid java.lang.IllegalArgumentException: parameter must be a descendant of this view
        viewBinding.tableNested.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

        with(viewBinding.confirmColumnsAction) {
            isVisible = isCheckBox
            if (isCheckBox) {
                setOnClickListener { dismiss() }
            }
        }
    }

    override fun onPause() {
        val selected = columnsAdapter.items.mapIndexed { index, cell ->
            cell.copy(answer = selectionHelper.isSelected(index))
        }
        (parentFragment as? Callback)
            ?.onSyncChosenColumnsWithParent(index, selected)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val select = List(columnsAdapter.items.size) { index ->
            selectionHelper.isSelected(index)
        }
        outState.putBooleanArray(ARG_SELECTED, select.toBooleanArray())
    }

    private fun handleColumnSelectionClick(cell: TableChoiceItem) {
        when (selectionHelper) {
            is SingleChoiceSelectionHelper -> {
                selectionHelper.select(columnsAdapter.items.indexOf(cell))
                dismiss()
            }

            is MultipleChoiceSelectionHelper ->
                selectionHelper.toggle(columnsAdapter.items.indexOf(cell))
        }
    }

    private fun mapSelection(choices: BooleanArray) {
        choices.forEachIndexed { index, isSelected ->
            if (isSelected) {
                selectionHelper.select(index)
            }
        }
    }

    interface Callback {
        fun onSyncChosenColumnsWithParent(index: Int, chosenRows: List<TableChoiceItem>)
    }
}