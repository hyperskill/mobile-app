package org.hyperskill.app.android.core.view.ui.adapter

import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.WidgetDataLoadingErrorBinding
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate

object DataLoadingErrorAdapterDelegate {
    // adapterDelegate function is used instead of creating a special class
    // because class casting requires a reified type
    inline operator fun <D, reified DT : D> invoke(
        crossinline onReloadClick: (DT) -> Unit
    ): AdapterDelegate<D, DelegateViewHolder<D>> =
        adapterDelegate<D, DT>(R.layout.widget_data_loading_error) {
            val viewBinding = WidgetDataLoadingErrorBinding.bind(itemView)
            viewBinding.reloadButton.setOnClickListener {
                item?.let(onReloadClick)
            }
        }
}