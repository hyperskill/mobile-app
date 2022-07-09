import Foundation
import shared

final class ProfileViewDataMapper {
    func mapProfileToViewData(_ profile: Profile) -> ProfileViewData {
        let role = profile.isStaff ? "JetBrains Academy Team" : "Learner"

        let aboutViewData = makeAboutViewData(profile: profile)

        return ProfileViewData(
            avatarSource: trimmedNonEmptyString(profile.avatar),
            fullname: profile.fullname,
            role: role,
            about: aboutViewData
        )
    }

    private func makeAboutViewData(profile: Profile) -> ProfileViewData.About {
        let livesInText: String? = {
            guard let countryCode = trimmedNonEmptyString(profile.country) else {
                return nil
            }

            let countryName = Formatter.localizedCoutryName(for: countryCode)

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
                .compactMap(Formatter.localizedLanguageName(for:))
                .map(\.capitalized)
            let formattedLanguages = (languagesNames.isEmpty ? languagesCodes : languagesNames).joined(separator: ", ")

            return "\(Strings.Profile.speaks) \(formattedLanguages)"
        }()

        return ProfileViewData.About(
            livesInText: livesInText,
            speaksText: speaksText,
            bio: trimmedNonEmptyString(profile.bio),
            experience: trimmedNonEmptyString(profile.experience),
            facebookUsername: trimmedNonEmptyString(profile.facebookUsername),
            twitterUsername: trimmedNonEmptyString(profile.twitterUsername),
            linkedInUsername: trimmedNonEmptyString(profile.linkedinUsername),
            redditUsername: trimmedNonEmptyString(profile.redditUsername),
            githubUsername: trimmedNonEmptyString(profile.githubUsername)
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
