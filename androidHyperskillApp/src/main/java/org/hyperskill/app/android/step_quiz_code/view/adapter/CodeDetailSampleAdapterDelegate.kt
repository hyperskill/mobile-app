package org.hyperskill.app.android.step_quiz_code.view.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemStepQuizCodeDetailSampleBinding
import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class CodeDetailSampleAdapterDelegate : AdapterDelegate<CodeDetail, DelegateViewHolder<CodeDetail>>() {
    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<CodeDetail> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_code_detail_sample))

    override fun isForViewType(position: Int, data: CodeDetail): Boolean =
        data is CodeDetail.Sample

    private class ViewHolder(root: View) : DelegateViewHolder<CodeDetail>(root) {
        private val viewBinding: ItemStepQuizCodeDetailSampleBinding by viewBinding(ItemStepQuizCodeDetailSampleBinding::bind)
        private val inputTitle = viewBinding.stepQuizCodeDetailSampleInputTitle
        private val outputTitle = viewBinding.stepQuizCodeDetailSampleOutputTitle
        private val input = viewBinding.stepQuizCodeDetailSampleInput
        private val output = viewBinding.stepQuizCodeDetailSampleOutput

        override fun onBind(data: CodeDetail) {
            data as CodeDetail.Sample

            inputTitle.text = context.getString(org.hyperskill.app.R.string.step_quiz_code_detail_sample_input_title, data.position)
            outputTitle.text = context.getString(org.hyperskill.app.R.string.step_quiz_code_detail_sample_output_title, data.position)
            input.text = data.input
            output.text = data.output
        }
    }
}