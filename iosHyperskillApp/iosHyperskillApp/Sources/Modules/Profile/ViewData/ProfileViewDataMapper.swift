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
            guard let country = trimmedNonEmptyString(profile.country) else {
                return nil
            }

            return "\(Strings.Profile.livesIn) \(country)"
        }()

        let speaksText: String? = {
            guard let languages = profile.languages else {
                return nil
            }

            if languages.isEmpty {
                return nil
            }

            return "\(Strings.Profile.speaks) \(languages.joined(separator: ","))"
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
