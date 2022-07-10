import Foundation
import shared

final class ProfileViewDataMapper {
    private let formatter: Formatter

    init(formatter: Formatter) {
        self.formatter = formatter
    }

    func mapProfileToViewData(_ profile: Profile) -> ProfileViewData {
        let role = profile.isStaff ? Strings.Profile.roleStaff : Strings.Profile.roleLearner

        let livesInText: String? = {
            guard let countryCode = trimmedNonEmptyString(profile.country) else {
                return nil
            }

            let countryName = formatter.localizedCoutryName(for: countryCode)

            return "\(Strings.Profile.livesIn) \(countryName ?? countryCode)"
        }()

        let speaksText: String? = {
            guard let languagesCodes = profile.languages else {
                return nil
            }

            if languagesCodes.isEmpty {
                return nil
            }

            let languagesNames = languagesCodes
                .compactMap(formatter.localizedLanguageName(for:))
                .map(\.capitalized)
            let formattedLanguages = (languagesNames.isEmpty ? languagesCodes : languagesNames).joined(separator: ", ")

            return "\(Strings.Profile.speaks) \(formattedLanguages)"
        }()

        let socialAccounts: [ProfileSocialAccount] = {
            var result = [ProfileSocialAccount]()

            if let facebookUsername = trimmedNonEmptyString(profile.facebookUsername) {
                result.append(.init(type: .facebook, username: facebookUsername))
            }
            if let twitterUsername = trimmedNonEmptyString(profile.twitterUsername) {
                result.append(.init(type: .twitter, username: twitterUsername))
            }
            if let linkedinUsername = trimmedNonEmptyString(profile.linkedinUsername) {
                result.append(.init(type: .linkedIn, username: linkedinUsername))
            }
            if let redditUsername = trimmedNonEmptyString(profile.redditUsername) {
                result.append(.init(type: .reddit, username: redditUsername))
            }
            if let githubUsername = trimmedNonEmptyString(profile.githubUsername) {
                result.append(.init(type: .github, username: githubUsername))
            }

            return result
        }()

        return ProfileViewData(
            avatarSource: trimmedNonEmptyString(profile.avatar),
            fullname: profile.fullname,
            role: role,
            livesInText: livesInText,
            speaksText: speaksText,
            bio: trimmedNonEmptyString(profile.bio),
            experience: trimmedNonEmptyString(profile.experience),
            socialAccounts: socialAccounts
        )
    }

    private func trimmedNonEmptyString(_ stringOrNil: String?) -> String? {
        guard let string = stringOrNil else {
            return nil
        }

        let trimmedString = string.trimmed()

        return trimmedString.isEmpty ? nil : trimmedString
    }
}
