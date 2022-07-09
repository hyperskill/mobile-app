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

    init(viewModel: ProfileViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            ZStack {
                BackgroundView(color: .systemGroupedBackground)
                buildBody()
            }
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

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is ProfileFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.loadProfile()
                }
        case is ProfileFeatureStateLoading:
            ProgressView()
        case is ProfileFeatureStateError:
            PlaceholderView(
                configuration: .networkError {
                    viewModel.loadProfile(forceUpdate: true)
                }
            )
        case let content as ProfileFeatureStateContent:
            let viewData = viewModel.makeViewData(content.profile)

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
        default:
            Text("Unkwown state")
        }
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
