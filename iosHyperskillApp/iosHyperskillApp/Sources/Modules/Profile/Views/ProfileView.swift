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

                    if let streak = content.streak {
                        StreakAssembly(streak: streak)
                            .makeModule()
                            .padding(.horizontal)
                    }

                    ProfileAboutView(
                        livesInText: viewData.livesInText,
                        speaksText: viewData.speaksText,
                        bio: viewData.bio,
                        experience: viewData.experience,
                        socialAccounts: viewData.socialAccounts,
                        onSocialAccountTapped: viewModel.presentSocialAccount(_:),
                        onFullVersionButtonTapped: viewModel.presentProfileFullVersion
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
        ProfileAssembly.currentUser().makeModule()
    }
}
