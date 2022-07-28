package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id")
    val id: Long,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("badge_title")
    val badgeTitle: String,
    @SerialName("bio")
    val bio: String,
    @SerialName("fullname")
    val fullname: String,
//    @SerialName("gamification")
//    val gamification: Gamification,
    @SerialName("invitation_code")
    val invitationCode: String?,
    @SerialName("comments_posted")
    val postedComments: PostedComments,
    @SerialName("username")
    val username: String,
    @SerialName("completed_tracks")
    val completedTracks: List<Long>,
    @SerialName("country")
    val country: String?,
    @SerialName("languages")
    val languages: List<String>?,
    @SerialName("experience")
    val experience: String,
    @SerialName("github_username")
    val githubUsername: String,
    @SerialName("linkedin_username")
    val linkedinUsername: String,
    @SerialName("twitter_username")
    val twitterUsername: String,
    @SerialName("reddit_username")
    val redditUsername: String,
    @SerialName("facebook_username")
    val facebookUsername: String,
    @SerialName("discord_id")
    val discordId: Long?,
    @SerialName("visibility")
    val visibility: String,
    @SerialName("cover")
    val cover: String,
    @SerialName("selected_tracks")
    val selectedTracks: List<Long>,
    @SerialName("daily_pace_minutes")
    val dailyPaceMinutes: Int,
    @SerialName("daily_step")
    val dailyStep: Long?,
    @SerialName("date_registered")
    val registrationDate: String?,
    @SerialName("email")
    val email: String,
    @SerialName("kind")
    val kind: String,
//    @SerialName("features")
//    val features: Features,
    @SerialName("is_anonymous")
    val isAnonymous: Boolean,
    @SerialName("can_issue_certificate")
    val canIssueCertificate: Boolean,
    @SerialName("is_beta")
    val isBeta: Boolean,
    @SerialName("is_biased")
    val isBiased: Boolean,
    @SerialName("is_diagnosed")
    val isDiagnosed: Boolean,
    @SerialName("is_email_verified")
    val isEmailVerified: Boolean,
    @SerialName("is_guest")
    val isGuest: Boolean,
    @SerialName("is_terms_accepted")
    val isTermsAccepted: Boolean,
    @SerialName("is_staff")
    val isStaff: Boolean,
    @SerialName("is_subscribed_to_notifications")
    val isSubscribedToNotifications: Boolean,
    @SerialName("is_testee")
    val isTestee: Boolean,
    @SerialName("track_id")
    val trackId: Long?,
    @SerialName("track_title")
    val trackTitle: String?,
    @SerialName("project")
    val project: Long?,
    @SerialName("subscribed_for_marketing")
    val subscribedForMarketing: Boolean,
    @SerialName("free_till")
    val freeTill: String?,
    @SerialName("isic_id")
    val isicId: String,
    @SerialName("isic_name")
    val isicName: String,
    @SerialName("isic_status")
    val isicStatus: Long
)
