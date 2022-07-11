import Foundation

struct ProfileSocialAccount {
    let type: SocialAccount
    let username: String

    var profileURL: URL? {
        URL(string: "\(type.profilePrefix)/\(username)")
    }

    enum SocialAccount: CaseIterable {
        case facebook
        case twitter
        case linkedIn
        case reddit
        case github

        fileprivate var profilePrefix: String {
            switch self {
            case .facebook:
                return "https://www.facebook.com"
            case .twitter:
                return "https://twitter.com"
            case .linkedIn:
                return "https://www.linkedin.com/in"
            case .reddit:
                return "https://www.reddit.com/user"
            case .github:
                return "https://github.com"
            }
        }
    }
}
