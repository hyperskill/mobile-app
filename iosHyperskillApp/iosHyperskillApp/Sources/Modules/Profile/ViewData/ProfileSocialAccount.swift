import Foundation
import shared

struct ProfileSocialAccount {
    let type: SocialAccount
    let username: String

    var profileURL: URL? {
        URL(string: "\(type.profilePrefix)\(username)")
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
                return SocialNetworksRedirect.facebook.baseUrl
            case .twitter:
                return SocialNetworksRedirect.twitter.baseUrl
            case .linkedIn:
                return SocialNetworksRedirect.linkedin.baseUrl
            case .reddit:
                return SocialNetworksRedirect.reddit.baseUrl
            case .github:
                return SocialNetworksRedirect.github.baseUrl
            }
        }
    }
}
