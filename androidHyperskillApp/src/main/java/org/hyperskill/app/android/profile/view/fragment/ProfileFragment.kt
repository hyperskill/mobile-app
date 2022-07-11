package org.hyperskill.app.android.profile.view.fragment

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.transform.CircleCropTransformation
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentProfileBinding
import org.hyperskill.app.android.streak.view.delegate.StreakCardFormDelegate
import org.hyperskill.app.android.view.base.ui.extension.redirectToUsernamePage
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.view.social_redirect.SocialNetworksRedirect
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.presentation.ProfileViewModel
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import java.util.Locale

class ProfileFragment :
    Fragment(R.layout.fragment_profile),
    ReduxView<ProfileFeature.State, ProfileFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            ProfileFragment()
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val profileViewModel: ProfileViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<ProfileFeature.State> = ViewStateDelegate()

    private lateinit var streakFormDelegate: StreakCardFormDelegate

    private lateinit var profile: Profile
    private lateinit var streak: Streak

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        viewBinding.profileError.tryAgain.setOnClickListener {
            profileViewModel.onNewMessage(ProfileFeature.Message.Init(forceUpdate = true))
        }

        profileViewModel.onNewMessage(ProfileFeature.Message.Init())
    }

    private fun injectComponents() {
        val profileComponent = HyperskillApp.graph().buildProfileComponent()
        val platformProfileComponent = HyperskillApp.graph().buildPlatformProfileComponent(profileComponent)
        viewModelFactory = platformProfileComponent.reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        with(viewStateDelegate) {
            addState<ProfileFeature.State.Idle>()
            addState<ProfileFeature.State.Loading>(viewBinding.profileProgress)
            addState<ProfileFeature.State.Error>(viewBinding.profileError.root)
            addState<ProfileFeature.State.Content>(viewBinding.profileContainer)
        }
    }

    override fun onAction(action: ProfileFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: ProfileFeature.State) {
        viewStateDelegate.switchState(state)
        when (state) {
            is ProfileFeature.State.Content -> {
                profile = state.profile
                streak = state.streak ?: Streak(profile.id, "", 0, 0, false, emptyList())
                setupProfile()
            }
        }
    }

    private fun setupProfile() {
        streakFormDelegate = StreakCardFormDelegate(requireContext(), viewBinding.profileStreakLayout, streak)

        setupNameProfileBadge()
        setupRemindersSchedule()
        setupAboutMeSection()
        setupBioSection()
        setupExperienceSection()
        setupSocialButtons()
        setupProfileBrowserRedirect()
    }

    private fun setupNameProfileBadge() {
        val svgImageLoader = ImageLoader.Builder(requireContext())
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        viewBinding.profileAvatarImageView.load(profile.avatar, svgImageLoader) {
            transformations(CircleCropTransformation())
        }
        viewBinding.profileNameTextView.text = profile.fullname

        if (profile.isStaff) {
            viewBinding.profileRoleTextView.text = resources.getString(R.string.profile_role_staff_text)
        } else {
            viewBinding.profileRoleTextView.text = resources.getString(R.string.profile_role_learner_text)
        }
    }

    private fun setupRemindersSchedule() {
        // TODO
        if (viewBinding.profileDailyRemindersSwitchCompat.isChecked) {
            viewBinding.profileScheduleTextView.visibility = View.VISIBLE
        } else {
            viewBinding.profileScheduleTextView.visibility = View.GONE
        }

        viewBinding.profileDailyRemindersSwitchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewBinding.profileScheduleTextView.visibility = View.VISIBLE
            } else {
                viewBinding.profileScheduleTextView.visibility = View.GONE
            }
        }
    }

    private fun setupAboutMeSection() {
        if (profile.country != null) {
            viewBinding.profileAboutLivesTextView.text =
                "${resources.getString(R.string.profile_lives_in_text)} ${ Locale(Locale.ENGLISH.language, profile.country).displayCountry }"
        } else {
            viewBinding.profileAboutLivesTextView.visibility = View.GONE
        }

        if (profile.languages?.isEmpty() == false) {
            viewBinding.profileAboutSpeaksTextView.text =
                "${resources.getString(R.string.profile_speaks_text)} ${profile.languages!!.joinToString(", ") { Locale(it).getDisplayLanguage(Locale.ENGLISH) }}"
        } else {
            viewBinding.profileAboutSpeaksTextView.visibility = View.GONE
        }
    }

    private fun setupBioSection() {
        if (profile.bio != "") {
            viewBinding.profileAboutBioTextTextView.text = profile.bio
        } else {
            viewBinding.profileAboutBioTextTextView.visibility = View.GONE
            viewBinding.profileAboutBioBarTextView.visibility = View.GONE
        }
    }

    private fun setupExperienceSection() {
        if (profile.experience != "") {
            viewBinding.profileAboutExperienceTextTextView.text = profile.experience
        } else {
            viewBinding.profileAboutExperienceTextTextView.visibility = View.GONE
            viewBinding.profileAboutExperienceBarTextView.visibility = View.GONE
        }
    }

    private fun setupSocialButtons() {
        with(viewBinding) {
            if (profile.facebookUsername != "") {
                profileFacebookButton.setOnClickListener {
                    SocialNetworksRedirect.FACEBOOK.redirectToUsernamePage(requireContext(), profile.facebookUsername)
                }
            } else {
                profileFacebookButton.visibility = View.GONE
            }

            if (profile.twitterUsername != "") {
                profileTwitterButton.setOnClickListener {
                    SocialNetworksRedirect.TWITTER.redirectToUsernamePage(requireContext(), profile.twitterUsername)
                }
            } else {
                profileTwitterButton.visibility = View.GONE
            }

            if (profile.linkedinUsername != "") {
                profileLinkedinButton.setOnClickListener {
                    SocialNetworksRedirect.LINKEDIN.redirectToUsernamePage(requireContext(), profile.linkedinUsername)
                }
            } else {
                profileLinkedinButton.visibility = View.GONE
            }

            if (profile.redditUsername != "") {
                profileRedditButton.setOnClickListener {
                    SocialNetworksRedirect.REDDIT.redirectToUsernamePage(requireContext(), profile.redditUsername)
                }
            } else {
                profileRedditButton.visibility = View.GONE
            }

            if (profile.githubUsername != "") {
                profileGithubButton.setOnClickListener {
                    SocialNetworksRedirect.GITHUB.redirectToUsernamePage(requireContext(), profile.githubUsername)
                }
            } else {
                profileGithubButton.visibility = View.GONE
            }
        }
    }

    private fun setupProfileBrowserRedirect() {
        viewBinding.profileViewFullVersionTextView.paintFlags = viewBinding.profileViewFullVersionTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        viewBinding.profileViewFullVersionTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(BuildKonfig.BASE_URL + "profile/${profile.id}")
            startActivity(intent)
        }
    }
}