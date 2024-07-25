package org.hyperskill.profile

import org.hyperskill.app.profile.domain.model.FeatureValues
import org.hyperskill.app.profile.domain.model.Gamification
import org.hyperskill.app.profile.domain.model.Profile

fun Profile.Companion.stub(
    id: Long = 0,
    isBeta: Boolean = false,
    isGuest: Boolean = false,
    trackId: Long? = null,
    projectId: Long? = null,
    featureValues: FeatureValues = FeatureValues(),
    featuresMap: Map<String, Boolean> = emptyMap()
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
        trackId = trackId,
        trackTitle = null,
        projectId = projectId,
        isBeta = isBeta,
        featureValues = featureValues,
        featuresMap = featuresMap
    )