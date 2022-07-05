import SwiftUI

extension TrackView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset
        let spacingBetweenRelativeItems = LayoutInsets.smallInset
    }
}

struct TrackView: View {
    private(set) var appearance = Appearance()

    let viewData: TrackViewData

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    TrackHeaderView(
                        iconImageName: viewData.iconImageName,
                        name: viewData.name,
                        role: viewData.role
                    )

                    TrackAboutView(
                        rating: viewData.rating,
                        timeToComplete: viewData.timeToComplete,
                        projectsCount: viewData.projectsCount,
                        topicsCount: viewData.topicsCount,
                        description: viewData.description,
                        buttonText: viewData.buttonText,
                        onButtonTapped: {}
                    )
                }
                .padding(.vertical)
            }
            .frame(maxWidth: .infinity)
            .navigationTitle(Strings.Track.title)
            .background(BackgroundView())
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct TrackView_Previews: PreviewProvider {
    static var previews: some View {
        TrackAssembly().makeModule()
    }
}
