package org.hyperskill.app.android.profile.view.delegate

import android.content.Context
import android.view.View
import java.util.Locale
import org.hyperskill.app.android.databinding.LayoutProfilePersonalInfoBinding
import org.hyperskill.app.android.view.base.ui.extension.redirectToUsernamePage
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.view.social_redirect.SocialNetworksRedirect

object AboutMeDelegate {

    fun setup(
        viewBinding: LayoutProfilePersonalInfoBinding,
        onNewMessage: (ProfileFeature.Message) -> Unit
    ) {
        with(viewBinding.profileViewFullVersionTextView) {
            paintFlags = paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                onNewMessage(ProfileFeature.Message.ClickedViewFullProfile)
            }
        }
    }
    fun render(
        context: Context,
        viewBinding: LayoutProfilePersonalInfoBinding,
        profile: Profile
    ) {
        renderAboutMeSection(context, viewBinding, profile)
        renderBioSection(viewBinding, profile)
        renderExperienceSection(viewBinding, profile)
        renderSocialButtons(context, viewBinding, profile)
    }

    private fun renderAboutMeSection(
        context: Context,
        viewBinding: LayoutProfilePersonalInfoBinding,
        profile: Profile
    ) {
        with(viewBinding) {
            if (profile.country != null) {
                profileAboutLivesTextView.text =
                    "${context.resources.getString(org.hyperskill.app.R.string.profile_lives_in_text)} ${
                        Locale(
                            java.util.Locale.ENGLISH.language,
                            profile.country!!
                        ).displayCountry
                    }"
            } else {
                profileAboutLivesTextView.visibility = View.GONE
            }

            if (profile.languages?.isEmpty() == false) {
                val languages = profile.languages!!.joinToString(", ") {
                    Locale(it).getDisplayLanguage(Locale.ENGLISH)
                }
                profileAboutSpeaksTextView.text =
                    "${context.resources.getString(org.hyperskill.app.R.string.profile_speaks_text)} $languages"
            } else {
                profileAboutSpeaksTextView.visibility = View.GONE
            }
        }
    }

    private fun renderBioSection(
        viewBinding: LayoutProfilePersonalInfoBinding,
        profile: Profile
    ) {
        with(viewBinding) {
            if (profile.bio != "") {
                profileAboutBioTextTextView.text = profile.bio
            } else {
                profileAboutBioTextTextView.visibility = View.GONE
                profileAboutBioBarTextView.visibility = View.GONE
            }
        }
    }

    private fun renderExperienceSection(
        viewBinding: LayoutProfilePersonalInfoBinding,
        profile: Profile
    ) {
        with(viewBinding) {
            if (profile.experience != "") {
                profileAboutExperienceTextTextView.text = profile.experience
            } else {
                profileAboutExperienceTextTextView.visibility = View.GONE
                profileAboutExperienceBarTextView.visibility = View.GONE
            }
        }
    }

    private fun renderSocialButtons(context: Context, viewBinding: LayoutProfilePersonalInfoBinding, profile: Profile) {
        with(viewBinding) {
            if (profile.facebookUsername != "") {
                profileFacebookButton.setOnClickListener {
                    SocialNetworksRedirect.FACEBOOK.redirectToUsernamePage(context, profile.facebookUsername)
                }
            } else {
                profileFacebookButton.visibility = View.GONE
            }

            if (profile.twitterUsername != "") {
                profileTwitterButton.setOnClickListener {
                    SocialNetworksRedirect.TWITTER.redirectToUsernamePage(context, profile.twitterUsername)
                }
            } else {
                profileTwitterButton.visibility = View.GONE
            }

            if (profile.linkedinUsername != "") {
                profileLinkedinButton.setOnClickListener {
                    SocialNetworksRedirect.LINKEDIN.redirectToUsernamePage(context, profile.linkedinUsername)
                }
            } else {
                profileLinkedinButton.visibility = View.GONE
            }

            if (profile.redditUsername != "") {
                profileRedditButton.setOnClickListener {
                    SocialNetworksRedirect.REDDIT.redirectToUsernamePage(context, profile.redditUsername)
                }
            } else {
                profileRedditButton.visibility = View.GONE
            }

            if (profile.githubUsername != "") {
                profileGithubButton.setOnClickListener {
                    SocialNetworksRedirect.GITHUB.redirectToUsernamePage(context, profile.githubUsername)
                }
            } else {
                profileGithubButton.visibility = View.GONE
            }
        }
    }
}