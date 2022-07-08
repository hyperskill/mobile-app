import shared
import SwiftUI

extension ProfileView {
    struct Appearance {
        let spacingBetweenContainers: CGFloat = 20
    }
}

struct ProfileView: View {
    private(set) var appearance = Appearance()

    @State private var presentingSettings = false

    @ObservedObject private var viewModel: ProfileViewModel

    let viewData: ProfileViewData

    init(viewModel: ProfileViewModel, viewData: ProfileViewData) {
        self.viewModel = viewModel
        self.viewData = viewData
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    ProfileHeaderView(
                        avatarSource: viewData.avatarSource,
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
                        onFullVersionButtonTapped: {
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
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
    }

    private func handleViewAction(_ viewAction: ProfileFeatureActionViewAction) {
        print("ProfileView :: \(#function) viewAction = \(viewAction)")
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileAssembly(presentationDescription: .init(profileType: .currentUser)).makeModule()
    }
}
