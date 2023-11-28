package org.hyperskill.app.android.leaderboard.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.databinding.FragmentLeaderboardBinding
import org.hyperskill.app.android.databinding.LayoutGamificationToolbarBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.leaderboard.ui.LeaderboardScreen

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    companion object {
        fun newInstance(): LeaderboardFragment =
            LeaderboardFragment()
    }

    private val leaderboardViewBinding: FragmentLeaderboardBinding by viewBinding(FragmentLeaderboardBinding::bind)

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGamificationToolbar(leaderboardViewBinding.leaderboardAppBar)
        leaderboardViewBinding.leaderboardContent.setContent {
            HyperskillTheme {
                LeaderboardScreen()
            }
        }
    }

    private fun initGamificationToolbar(viewBinding: LayoutGamificationToolbarBinding) {
        viewBinding.gamificationToolbar.title =
            requireContext().getString(org.hyperskill.app.R.string.leaderboard_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            viewBinding = viewBinding
        ) { message ->
            TODO("Gamification toolbar message handling is not implemented yet")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gamificationToolbarDelegate = null
    }
}