package org.hyperskill.app.android.auth.view.ui.adapter.delegates

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.model.AuthSocialCardInfo
import org.hyperskill.app.android.databinding.ItemAuthMaterialCardViewBinding
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class AuthSocialAdapterDelegate(
    private val onItemClick: (AuthSocialCardInfo) -> Unit
) : AdapterDelegate<AuthSocialCardInfo, DelegateViewHolder<AuthSocialCardInfo>>() {
    override fun isForViewType(position: Int, data: AuthSocialCardInfo): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<AuthSocialCardInfo> =
        ViewHolder(createView(parent, R.layout.item_auth_material_card_view))

    inner class ViewHolder(root: View) : DelegateViewHolder<AuthSocialCardInfo>(root) {
        private val viewBinding: ItemAuthMaterialCardViewBinding by viewBinding {
            ItemAuthMaterialCardViewBinding.bind(
                root
            )
        }

        override fun onBind(data: AuthSocialCardInfo) {
            viewBinding.cardText.setText(data.textId)
            viewBinding.cardIcon.setImageResource(data.iconId)
            itemView.setOnClickListener {
                onItemClick.invoke(data)
            }
        }
    }
}