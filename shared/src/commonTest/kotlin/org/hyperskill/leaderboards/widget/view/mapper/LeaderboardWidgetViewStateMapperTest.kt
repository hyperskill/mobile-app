package org.hyperskill.leaderboards.widget.view.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.leaderboards.domain.model.LeaderboardItem
import org.hyperskill.app.leaderboards.widget.view.mapper.LeaderboardWidgetViewStateMapper
import org.hyperskill.app.leaderboards.widget.view.model.LeaderboardWidgetListItem
import org.hyperskill.app.users.domain.model.User

class LeaderboardWidgetViewStateMapperTest {
    private val viewStateMapper = LeaderboardWidgetViewStateMapper(
        resourceProvider = ResourceProviderStub()
    )

    @Test
    fun `leaderboard list contains separator if position between items greater than 1`() {
        val given = listOf(
            LeaderboardItem(
                user = User(
                    id = 1,
                    fullname = "User 1",
                    avatar = "https://some.url"
                ),
                position = 1,
                passedProblems = 1
            ),
            LeaderboardItem(
                user = User(
                    id = 2,
                    fullname = "User 2",
                    avatar = "https://some.url"
                ),
                position = 3,
                passedProblems = 1
            ),
            LeaderboardItem(
                user = User(
                    id = 3,
                    fullname = "User 3",
                    avatar = "https://some.url"
                ),
                position = 4,
                passedProblems = 1
            )
        )
        val expected = listOf(
            LeaderboardWidgetListItem.UserInfo(
                position = 1,
                passedProblems = 1,
                passedProblemsSubtitle = "",
                userAvatar = "https://some.url",
                username = "User 1",
                isCurrentUser = true
            ),
            LeaderboardWidgetListItem.Separator,
            LeaderboardWidgetListItem.UserInfo(
                position = 3,
                passedProblems = 1,
                passedProblemsSubtitle = "",
                userAvatar = "https://some.url",
                username = "User 2",
                isCurrentUser = false
            ),
            LeaderboardWidgetListItem.UserInfo(
                position = 4,
                passedProblems = 1,
                passedProblemsSubtitle = "",
                userAvatar = "https://some.url",
                username = "User 3",
                isCurrentUser = false
            )
        )
        val actual = viewStateMapper.mapLeaderboardList(
            list = given,
            currentUserId = 1
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `leaderboard list not contains separator if position between items after each other`() {
        val given = listOf(
            LeaderboardItem(
                user = User(
                    id = 1,
                    fullname = "User 1",
                    avatar = "https://some.url"
                ),
                position = 1,
                passedProblems = 1
            ),
            LeaderboardItem(
                user = User(
                    id = 2,
                    fullname = "User 2",
                    avatar = "https://some.url"
                ),
                position = 2,
                passedProblems = 1
            ),
            LeaderboardItem(
                user = User(
                    id = 3,
                    fullname = "User 3",
                    avatar = "https://some.url"
                ),
                position = 3,
                passedProblems = 1
            )
        )
        val expected = listOf(
            LeaderboardWidgetListItem.UserInfo(
                position = 1,
                passedProblems = 1,
                passedProblemsSubtitle = "",
                userAvatar = "https://some.url",
                username = "User 1",
                isCurrentUser = true
            ),
            LeaderboardWidgetListItem.UserInfo(
                position = 2,
                passedProblems = 1,
                passedProblemsSubtitle = "",
                userAvatar = "https://some.url",
                username = "User 2",
                isCurrentUser = false
            ),
            LeaderboardWidgetListItem.UserInfo(
                position = 3,
                passedProblems = 1,
                passedProblemsSubtitle = "",
                userAvatar = "https://some.url",
                username = "User 3",
                isCurrentUser = false
            )
        )
        val actual = viewStateMapper.mapLeaderboardList(
            list = given,
            currentUserId = 1
        )
        assertEquals(expected, actual)
    }
}