package org.hyperskill.app.android.profile.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStreakFreezeBinding
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.presentation.ProfileViewModel

class StreakFreezeDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val Tag = "StreakFreezeDialogFragment"
        private const val StateKey = "StreakFreezeStateKey"

        fun newInstance(streakFreezeState: ProfileFeature.StreakFreezeState): StreakFreezeDialogFragment =
            StreakFreezeDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(StateKey, streakFreezeState, serializer = ProfileFeature.StreakFreezeState.serializer())
                }
            }
    }

    private var streakFreezeState: ProfileFeature.StreakFreezeState? = null

    private val viewBinding by viewBinding(FragmentStreakFreezeBinding::bind)

    private val viewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        streakFreezeState = arguments?.getParcelable(
            key = StateKey,
            deserializer = ProfileFeature.StreakFreezeState.serializer()
        ) ?: error("streakFreezeState should not be null")
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    viewModel.onNewMessage(ProfileFeature.Message.StreakFreezeModalShownEventMessage)
                }
            }
            dialog.setOnDismissListener {
                viewModel.onNewMessage(ProfileFeature.Message.StreakFreezeModalHiddenEventMessage)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hack to apply AppTheme to content
        // Without contextThemeWrapper AppTheme is not applied
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        return inflater
            .cloneInContext(contextThemeWrapper)
            .inflate(
                R.layout.fragment_streak_freeze,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val state = requireNotNull(streakFreezeState)
        with(viewBinding) {
            streakFreezeImage.setImageResource(
                when (state) {
                    ProfileFeature.StreakFreezeState.AlreadyHave,
                    is ProfileFeature.StreakFreezeState.CanBuy -> R.drawable.ic_buy_streak_freeze_enough
                    is ProfileFeature.StreakFreezeState.NotEnoughGems -> R.drawable.ic_buy_streak_freeze_not_enough
                }
            )
            streakFreezeModalTitle.setText(
                when (state) {
                    ProfileFeature.StreakFreezeState.AlreadyHave ->
                        R.string.streak_freeze_modal_already_have_title
                    is ProfileFeature.StreakFreezeState.CanBuy ->
                        R.string.streak_freeze_modal_can_buy_title
                    is ProfileFeature.StreakFreezeState.NotEnoughGems ->
                        R.string.streak_freeze_modal_not_enough_gems_title
                }
            )
            with(streakFreezeCountTextView) {
                text = when (state) {
                    ProfileFeature.StreakFreezeState.AlreadyHave -> "1"
                    is ProfileFeature.StreakFreezeState.CanBuy -> state.price.toString()
                    is ProfileFeature.StreakFreezeState.NotEnoughGems -> state.price.toString()
                }
                setCompoundDrawablesWithIntrinsicBounds(
                    when (state) {
                        ProfileFeature.StreakFreezeState.AlreadyHave -> R.drawable.ic_buy_streak_freeze
                        is ProfileFeature.StreakFreezeState.CanBuy -> R.drawable.ic_gems_count
                        is ProfileFeature.StreakFreezeState.NotEnoughGems -> R.drawable.ic_gems_not_enough
                    }, // left
                    0,
                    0,
                    0
                )
            }
            streakFreezeCountDescription.setText(
                when (state) {
                    ProfileFeature.StreakFreezeState.AlreadyHave ->
                        R.string.streak_freeze_modal_you_have_one_day_streak_freeze
                    is ProfileFeature.StreakFreezeState.CanBuy,
                    is ProfileFeature.StreakFreezeState.NotEnoughGems ->
                        R.string.streak_freeze_modal_one_day_streak_freeze
                }
            )

            with(streakFreezeBuyButton) {
                isVisible = state is ProfileFeature.StreakFreezeState.CanBuy
                if (state is ProfileFeature.StreakFreezeState.CanBuy) {
                    streakFreezeBuyButton.text = resources.getString(
                        R.string.streak_freeze_modal_get_it_for_gems,
                        state.price
                    )
                }
                setOnClickListener(
                    when (state) {
                        is ProfileFeature.StreakFreezeState.CanBuy -> View.OnClickListener {
                            viewModel.onNewMessage(ProfileFeature.Message.StreakFreezeModalButtonClicked)
                        }
                        ProfileFeature.StreakFreezeState.AlreadyHave,
                        is ProfileFeature.StreakFreezeState.NotEnoughGems -> null
                    }
                )
            }

            with(streakFreezeContinueLearningButton) {
                isVisible = state !is ProfileFeature.StreakFreezeState.CanBuy
                setOnClickListener(
                    when (state) {
                        is ProfileFeature.StreakFreezeState.CanBuy -> null
                        ProfileFeature.StreakFreezeState.AlreadyHave,
                        is ProfileFeature.StreakFreezeState.NotEnoughGems -> View.OnClickListener {
                            viewModel.onNewMessage(ProfileFeature.Message.StreakFreezeModalButtonClicked)
                        }
                    }
                )
            }
        }
    }
}