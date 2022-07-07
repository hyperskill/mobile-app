import SwiftUI

extension ProfileView {
    struct Appearance {
        let spacingBetweenContainers: CGFloat = 20
    }
}

struct ProfileView: View {
    private(set) var appearance = Appearance()

    @State private var presentingSettings = false

    let viewData: ProfileViewData

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    ProfileHeaderView(
                        title: viewData.fullname,
                        subtitle: viewData.role
                    )

                    ProfileAboutView(
                        livesInText: viewData.about.livesInText,
                        speaksText: viewData.about.speaksText,
                        bio: viewData.about.bio,
                        experience: viewData.about.experience,
                        facebookUsername: viewData.about.facebookUsername,
                        twitterUsername: viewData.about.twitterUsername,
                        linkedInUsername: viewData.about.linkedInUsername,
                        redditUsername: viewData.about.redditUsername,
                        githubUsername: viewData.about.githubUsername,
                        onSocialAccountTapped: { socialAccount in
                            print("ProfileAboutView :: onSocialAccountTapped = \(socialAccount)")
                        },
                        buttonText: viewData.about.buttonText,
                        onButtonTapped: {
                            print("ProfileAboutView :: onButtonTapped")
                        }
                    )
                }
                .padding(.vertical)
            }
            .frame(maxWidth: .infinity)
            .background(BackgroundView(color: .systemGroupedBackground))
            .navigationTitle(Strings.Profile.title)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(
                        action: { presentingSettings = true },
                        label: { Image(systemName: "gear") }
                    )
                }
            }
            .sheet(isPresented: $presentingSettings) {
                SettingsAssembly().makeModule()
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileView(viewData: .placeholder)
    }
}
