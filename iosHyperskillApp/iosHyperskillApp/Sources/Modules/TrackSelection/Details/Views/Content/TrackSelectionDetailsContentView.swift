import SwiftUI

extension TrackSelectionDetailsContentView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let callToActionBlurInsets = LayoutInsets(
            horizontal: LayoutInsets.smallInset,
            vertical: -LayoutInsets.smallInset
        )

        func makeCallToActionButtonStyle(isEnabled: Bool) -> RoundedRectangleButtonStyle {
            var style = RoundedRectangleButtonStyle(style: .violet)
            style.backgroundDisabledOpacity = 1
            style.foregroundColor = isEnabled ? Color(ColorPalette.onPrimary) : Color(ColorPalette.onPrimaryAlpha60)
            return style
        }
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

    let isCallToActionButtonEnabled: Bool
    let onCallToActionButtonTap: () -> Void

    private let callToActionButtonFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    var body: some View {
        let callToActionButtonStyle = appearance.makeCallToActionButtonStyle(
            isEnabled: isCallToActionButtonEnabled
        )

        ScrollView {
            VStack(spacing: appearance.spacing) {
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
            .padding(.bottom, callToActionButtonStyle.minHeight)
        }
        .frame(maxWidth: .infinity)
        .navigationTitle(navigationTitle)
        .overlay(
            buildCallToActionButton(buttonStyle: callToActionButtonStyle),
            alignment: .bottom
        )
    }

    @MainActor
    @ViewBuilder
    private func buildCallToActionButton(
        buttonStyle: RoundedRectangleButtonStyle
    ) -> some View {
        Button(
            Strings.TrackSelectionDetails.callToActionButtonTitle,
            action: {
                callToActionButtonFeedbackGenerator.triggerFeedback()
                onCallToActionButtonTap()
            }
        )
        .buttonStyle(buttonStyle)
        .padding(.horizontal)
        .background(
            Color(ColorPalette.surface)
                .padding(appearance.callToActionBlurInsets.edgeInsets)
                .blur(radius: buttonStyle.cornerRadius)
        )
        .disabled(!isCallToActionButtonEnabled)
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
                    otherProvidersDescription: "Edvancium, Basic",
                    isCallToActionButtonEnabled: false,
                    onCallToActionButtonTap: {}
                )
            }
        }
    }
}
#endif
