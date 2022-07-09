import Foundation

struct ProfileViewData {
    let avatarSource: String?
    let fullname: String
    let role: String

    let livesInText: String?
    let speaksText: String?

    let bio: String?

    let experience: String?

    let socialAccounts: [ProfileSocialAccount]
}
