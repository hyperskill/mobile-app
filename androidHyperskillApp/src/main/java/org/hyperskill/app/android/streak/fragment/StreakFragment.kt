package org.hyperskill.app.android.streak.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStreakCardBinding
import org.hyperskill.app.android.streak.delegate.StreakCardFormDelegate

// TODO test fragment, remove before merging to develop
class StreakFragment : Fragment(R.layout.layout_streak_card) {

    private val viewBinding: LayoutStreakCardBinding by viewBinding(LayoutStreakCardBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val delegate = StreakCardFormDelegate(viewBinding)
    }
}