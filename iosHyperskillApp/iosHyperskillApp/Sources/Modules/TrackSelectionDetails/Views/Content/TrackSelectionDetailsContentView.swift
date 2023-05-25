import SwiftUI

extension TrackSelectionDetailsContentView {
    struct Appearance {
        let callToActionButtonStyle = RoundedRectangleButtonStyle(style: .violet)
    }
}

struct TrackSelectionDetailsContentView: View {
    private(set) var appearance = Appearance()

    let navigationTitle: String

    let description: String?
    let isBeta: Bool
    let isCompleted: Bool
    let isSelected: Bool

    let rating: String
    let timeToComplete: String?
    let topicsCount: String
    let projectsCount: String?
    let isCertificateAvailable: Bool

    let mainProviderTitle: String?
    let mainProviderDescription: String?
    let otherProvidersDescription: String?

    @ViewBuilder
    private var callToActionButton: some View {
        Button(
            "Select this track",
            action: {}
        )
        .buttonStyle(appearance.callToActionButtonStyle)
        .padding(.horizontal)
        .background(
            Color(ColorPalette.surface)
                .blur(radius: appearance.callToActionButtonStyle.cornerRadius)
        )
    }

    var body: some View {
        ScrollView {
            VStack(spacing: LayoutInsets.defaultInset) {
                TrackSelectionDetailsDescriptionView(
                    description: description,
                    isBeta: isBeta,
                    isCompleted: isCompleted,
                    isSelected: isSelected
                )

                TrackSelectionDetailsTrackOverviewView(
                    rating: rating,
                    timeToComplete: timeToComplete,
                    topicsCount: topicsCount,
                    projectsCount: projectsCount,
                    isCertificateAvailable: isCertificateAvailable
                )

                TrackSelectionDetailsProvidersView(
                    mainProviderTitle: mainProviderTitle,
                    mainProviderDescription: mainProviderDescription,
                    otherProvidersDescription: otherProvidersDescription
                )
            }
            .padding()
            .padding(.bottom, appearance.callToActionButtonStyle.minHeight)
        }
        .frame(maxWidth: .infinity)
        .navigationTitle(navigationTitle)
        .overlay(callToActionButton, alignment: .bottom)
    }
}

#if DEBUG
struct TrackSelectionDetailsContentView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ZStack {
                BackgroundView(color: .systemGroupedBackground)

                TrackSelectionDetailsContentView(
                    navigationTitle: "Python Core",
                    description: """
Acquire key Python skills to establish a solid foundation for pursuing a career in Backend Development or Data Science.
""",
                    isBeta: true,
                    isCompleted: true,
                    isSelected: true,
                    rating: "It's new track, no rating yet",
                    timeToComplete: "249 hours for all learning activities",
                    topicsCount: "154 topics with theory and practice adapted to you level",
                    projectsCount: "13 projects to choose from for your portfolio",
                    isCertificateAvailable: true,
                    mainProviderTitle: "JetBrains Academy",
                    mainProviderDescription: """
Learn to program by creating real-world applications. \
Empowered by a personalized study plan, interactive projects, and integration with JetBrains IDEs, \
you’ll gain hands-on programming experience that is essential for your career as a developer.
""",
                    otherProvidersDescription: "Edvancium, Basic"
                )
            }
        }
    }
}
#endif
