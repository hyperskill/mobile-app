import SwiftUI

extension ProfileAboutSocialAccountsView {
    struct Appearance {
        let containerWidthHeight: CGFloat = 44
    }
}

struct ProfileAboutSocialAccountsView: View {
    private(set) var appearance = Appearance()

    let availableSocialAccounts: [ProfileSocialAccount]

    var onSocialAccountTapped: (ProfileSocialAccount) -> Void

    var body: some View {
        HStack(spacing: 0) {
            ForEach(availableSocialAccounts, id: \.type) { socialAccount in
                Button(
                    action: {
                        onSocialAccountTapped(socialAccount)
                    },
                    label: {
                        Image(socialAccount.type.imageName)
                            .resizable()
                            .renderingMode(.template)
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: socialAccount.type.imageWidthHeight)
                            .foregroundColor(.secondaryText)
                    }
                )
                .frame(widthHeight: appearance.containerWidthHeight)
            }
        }
    }
}

fileprivate extension ProfileSocialAccount.SocialAccount {
    var imageName: String {
        switch self {
        case .facebook:
            return Images.Profile.About.Social.facebook
        case .twitter:
            return Images.Profile.About.Social.twitter
        case .linkedIn:
            return Images.Profile.About.Social.linkedIn
        case .reddit:
            return Images.Profile.About.Social.reddit
        case .github:
            return Images.Profile.About.Social.github
        }
    }

    var imageWidthHeight: CGFloat {
        switch self {
        case .reddit:
            return 26
        default:
            return 24
        }
    }
}

struct ProfileAboutSocialAccountsView_Previews: PreviewProvider {
    static var previews: some View {
        let availableSocialAccounts = ProfileSocialAccount.SocialAccount
            .allCases
            .map { ProfileSocialAccount(type: $0, username: "") }

        return Group {
            ProfileAboutSocialAccountsView(
                availableSocialAccounts: availableSocialAccounts,
                onSocialAccountTapped: { _ in }
            )

            ProfileAboutSocialAccountsView(
                availableSocialAccounts: availableSocialAccounts,
                onSocialAccountTapped: { _ in }
            )
            .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
