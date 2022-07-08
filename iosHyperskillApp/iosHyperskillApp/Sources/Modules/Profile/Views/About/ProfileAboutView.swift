import SwiftUI

extension ProfileAboutView {
    struct Appearance {
        let socialAccountsOffset = CGSize(width: -9, height: 0)
    }
}

struct ProfileAboutView: View {
    private(set) var appearance = Appearance()

    let livesInText: String?
    let speaksText: String?

    let bio: String?

    let experience: String?

    let facebookUsername: String?
    let twitterUsername: String?
    let linkedInUsername: String?
    let redditUsername: String?
    let githubUsername: String?
    var onSocialAccountTapped: (ProfileAboutSocialAccountsView.SocialAccount) -> Void

    var onFullVersionButtonTapped: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            Text(Strings.Profile.aboutMe)
                .font(.title3)
                .foregroundColor(.primaryText)

            if let livesInText = livesInText {
                Text(livesInText)
                    .font(.body)
                    .foregroundColor(.secondaryText)
            }

            if let speaksText = speaksText {
                Text(speaksText)
                    .font(.body)
                    .foregroundColor(.secondaryText)
            }

            if let bio = bio {
                VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                    Text(Strings.Profile.bio)
                        .font(.headline)
                        .foregroundColor(.primaryText)

                    Text(bio)
                        .font(.body)
                        .foregroundColor(.secondaryText)
                }
            }

            if let experience = experience {
                VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                    Text(Strings.Profile.experience)
                        .font(.headline)
                        .foregroundColor(.primaryText)

                    Text(experience)
                        .font(.body)
                        .foregroundColor(.secondaryText)
                }
            }

            if !availableSocialAccounts.isEmpty {
                ProfileAboutSocialAccountsView(
                    availableSocialAccounts: availableSocialAccounts,
                    onSocialAccountTapped: onSocialAccountTapped
                )
                .offset(appearance.socialAccountsOffset)
            }

            Spacer()

            Button(
                action: onFullVersionButtonTapped,
                label: { Text(Strings.Profile.viewFullVersionButton).underline() }
            )
            .font(.body)
            .foregroundColor(.secondaryText)
        }
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }

    private var availableSocialAccounts: [ProfileAboutSocialAccountsView.SocialAccount] {
        var result = [ProfileAboutSocialAccountsView.SocialAccount]()

        if facebookUsername != nil {
            result.append(.facebook)
        }
        if twitterUsername != nil {
            result.append(.twitter)
        }
        if linkedInUsername != nil {
            result.append(.linkedIn)
        }
        if redditUsername != nil {
            result.append(.reddit)
        }
        if githubUsername != nil {
            result.append(.github)
        }

        return result
    }
}

struct ProfileAboutView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileAboutView(
            livesInText: "Lives in India",
            speaksText: "Speaks English, Hindi",
            bio: """
Hey, I've been working out here a while. It's really hard so far, but I'm hanging in there. Hopefully, in time, I'll
finally learn to do these tasks faster than anyone else. Anyway, good luck to you and me! Thank you!
""",
            experience: """
I’ve learned so much spending in my mid-school. And I learned even more at the high-school. Amazing experience!
""",
            facebookUsername: "test",
            twitterUsername: "test",
            linkedInUsername: "test",
            redditUsername: "test",
            githubUsername: "test",
            onSocialAccountTapped: { _ in },
            onFullVersionButtonTapped: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
