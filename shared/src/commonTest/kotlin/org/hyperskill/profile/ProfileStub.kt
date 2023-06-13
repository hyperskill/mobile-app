package org.hyperskill.profile

import org.hyperskill.app.profile.domain.model.FeatureKeys
import org.hyperskill.app.profile.domain.model.Gamification
import org.hyperskill.app.profile.domain.model.Profile

fun Profile.Companion.stub(
    id: Long = 0,
    isBeta: Boolean = false,
    isGuest: Boolean = false,
    isStreakRecoveryFeatureEnabled: Boolean = false
): Profile =
    Profile(
        id = id,
        avatar = "",
        bio = "",
        fullname = "",
        gamification = Gamification(
            hypercoinsBalance = 0,
            passedProjectsCount = 0
        ),
        completedTracks = listOf(),
        country = null,
        languages = listOf(),
        experience = "",
        githubUsername = "",
        linkedinUsername = "",
        twitterUsername = "",
        redditUsername = "",
        facebookUsername = "",
        dailyStep = null,
        isGuest = isGuest,
        isStaff = false,
        trackId = null,
        trackTitle = null,
        isBeta = isBeta,
        featuresMap = mapOf(FeatureKeys.USER_STREAK_RECOVER to isStreakRecoveryFeatureEnabled)
    )