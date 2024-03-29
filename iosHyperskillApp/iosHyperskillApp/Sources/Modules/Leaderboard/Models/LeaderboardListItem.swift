import Foundation
import shared

struct LeaderboardListItem: Hashable {
    let position: Int
    let passedProblems: Int
    let passedProblemsSubtitle: String
    let userId: Int
    let userAvatar: String
    let username: String
    let isCurrentUser: Bool
}

extension LeaderboardListItem {
    init(sharedListItem: LeaderboardWidgetListItemUserInfo) {
        self.init(
            position: Int(sharedListItem.position),
            passedProblems: Int(sharedListItem.passedProblems),
            passedProblemsSubtitle: sharedListItem.passedProblemsSubtitle,
            userId: Int(sharedListItem.userId),
            userAvatar: sharedListItem.userAvatar,
            username: sharedListItem.username,
            isCurrentUser: sharedListItem.isCurrentUser
        )
    }
}

extension LeaderboardListItem {
    var shared: LeaderboardWidgetListItemUserInfo {
        LeaderboardWidgetListItemUserInfo(
            position: Int32(position),
            passedProblems: Int32(passedProblems),
            passedProblemsSubtitle: passedProblemsSubtitle,
            userId: Int64(userId),
            userAvatar: userAvatar,
            username: username,
            isCurrentUser: isCurrentUser
        )
    }
}
