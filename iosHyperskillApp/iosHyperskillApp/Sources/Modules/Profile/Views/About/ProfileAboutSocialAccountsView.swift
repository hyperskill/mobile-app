import SwiftUI

extension ProfileAboutSocialAccountsView {
    struct Appearance {
        let containerWidthHeight: CGFloat = 44
    }
}

struct ProfileAboutSocialAccountsView: View {
    private(set) var appearance = Appearance()

    let availableSocialAccounts: [SocialAccount]

    var onSocialAccountTapped: (SocialAccount) -> Void

    var body: some View {
        HStack(spacing: 0) {
            ForEach(availableSocialAccounts, id: \.self) { socialAccount in
                Button(
                    action: {
                        onSocialAccountTapped(socialAccount)
                    },
                    label: {
                        Image(socialAccount.imageName)
                            .resizable()
                            .renderingMode(.template)
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: socialAccount.imageWidthHeight)
                            .foregroundColor(.secondaryText)
                    }
                )
                .frame(widthHeight: appearance.containerWidthHeight)
            }
        }
    }

    enum SocialAccount: CaseIterable {
        case facebook
        case twitter
        case linkedIn
        case reddit
        case github

        fileprivate var imageName: String {
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

        fileprivate var imageWidthHeight: CGFloat {
            switch self {
            case .reddit:
                return 26
            default:
                return 24
            }
        }
    }
}

struct ProfileAboutSocialAccountsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProfileAboutSocialAccountsView(
                availableSocialAccounts: ProfileAboutSocialAccountsView.SocialAccount.allCases,
                onSocialAccountTapped: { _ in }
            )

            ProfileAboutSocialAccountsView(
                availableSocialAccounts: ProfileAboutSocialAccountsView.SocialAccount.allCases,
                onSocialAccountTapped: { _ in }
            )
            .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
