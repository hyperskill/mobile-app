package org.hyperskill.app.android.stage_implementation.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.serialization.Serializable
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentProjectCompletedBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.stage_implementation.presentation.StageImplementationViewModel

class ProjectCompletedBottomSheet : BottomSheetDialogFragment() {
    companion object {
        const val TAG: String = "ProjectCompletedBottomSheet"
        fun newInstance(params: Params): ProjectCompletedBottomSheet =
            ProjectCompletedBottomSheet().apply {
                this.params = params
            }
    }

    private var params: Params by argument(Params.serializer())

    private val viewModel: StageImplementationViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val viewBinding: FragmentProjectCompletedBinding by viewBinding(FragmentProjectCompletedBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    viewModel.onNewMessage(
                        StageImplementFeature.Message.ProjectCompletedModalShownEventMessage
                    )
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_project_completed,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            projectCompletedGemsAmountForStageCompletionTextView.text = "+${params.stageAward}"
            projectCompletedGemsAmountForProjectCompletionTextView.text = "+${params.projectAward}"

            projectCompletedGoToStudyPlanButton.setOnClickListener {
                viewModel.onNewMessage(
                    StageImplementFeature.Message.ProjectCompletedModalGoToStudyPlanClicked
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onNewMessage(
            StageImplementFeature.Message.ProjectCompletedModalHiddenEventMessage
        )
    }

    @Serializable
    data class Params(
        val stageAward: Int,
        val projectAward: Int
    )
}