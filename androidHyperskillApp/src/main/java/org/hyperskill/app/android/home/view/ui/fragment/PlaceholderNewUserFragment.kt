package org.hyperskill.app.android.home.view.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.config.BuildKonfig

class PlaceholderNewUserFragment : Fragment(R.layout.fragment_placeholder_new_user_screen) {
    companion object {
        fun newInstance(): Fragment =
            PlaceholderNewUserFragment()
    }

    private val viewBinding: FragmentPlaceholderNewUserScreenBinding by viewBinding(
        FragmentPlaceholderNewUserScreenBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.placeholderContinueToHyperskillButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(BuildKonfig.BASE_URL)
            startActivity(intent)
        }
    }
}