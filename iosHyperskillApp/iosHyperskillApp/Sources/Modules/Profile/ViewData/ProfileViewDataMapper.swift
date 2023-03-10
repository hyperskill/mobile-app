import Foundation
import shared

final class ProfileViewDataMapper {
    func mapProfileToViewData(
        _ profile: Profile,
        isDailyStudyRemindersEnabled: Bool,
        dailyStudyRemindersStartHour: Int,
        notificationPermissionStatus: NotificationPermissionStatus?
    ) -> ProfileViewData {
        let role = profile.isStaff ? Strings.Profile.roleStaff : Strings.Profile.roleLearner

        let livesInText: String? = {
            guard let countryCode = profile.country?.trimmedNonEmptyOrNil() else {
                return nil
            }

            let countryName = Formatter.localizedCountryName(for: countryCode)

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

        let socialAccounts: [ProfileSocialAccount] = {
            var result = [ProfileSocialAccount]()

            if let facebookUsername = profile.facebookUsername.trimmedNonEmptyOrNil() {
                result.append(.init(type: .facebook, username: facebookUsername))
            }
            if let twitterUsername = profile.twitterUsername.trimmedNonEmptyOrNil() {
                result.append(.init(type: .twitter, username: twitterUsername))
            }
            if let linkedinUsername = profile.linkedinUsername.trimmedNonEmptyOrNil() {
                result.append(.init(type: .linkedIn, username: linkedinUsername))
            }
            if let redditUsername = profile.redditUsername.trimmedNonEmptyOrNil() {
                result.append(.init(type: .reddit, username: redditUsername))
            }
            if let githubUsername = profile.githubUsername.trimmedNonEmptyOrNil() {
                result.append(.init(type: .github, username: githubUsername))
            }

            return result
        }()

        let isDailyStudyRemindersFullyEnabled =
          isDailyStudyRemindersEnabled && notificationPermissionStatus?.isRegistered ?? false

        return ProfileViewData(
            avatarSource: profile.avatar.trimmedNonEmptyOrNil(),
            fullname: profile.fullname,
            role: role,
            livesInText: livesInText,
            speaksText: speaksText,
            bio: profile.bio.trimmedNonEmptyOrNil(),
            experience: profile.experience.trimmedNonEmptyOrNil(),
            socialAccounts: socialAccounts,
            isDailyStudyRemindersEnabled: isDailyStudyRemindersFullyEnabled,
            dailyStudyRemindersStartHour: dailyStudyRemindersStartHour
        )
    }
}
