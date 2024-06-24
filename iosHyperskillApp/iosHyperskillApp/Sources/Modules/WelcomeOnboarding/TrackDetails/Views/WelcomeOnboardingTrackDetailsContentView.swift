import SwiftUI

extension WelcomeOnboardingTrackDetailsContentView {
    struct Appearance {
        let trackImageMaxHeight: CGFloat = 234

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct WelcomeOnboardingTrackDetailsContentView: View {
    private(set) var appearance = Appearance()

    let title: String

    let trackImageResource: ImageResource?
    let trackTitle: String
    let trackDescription: String

    let subtitle: String
    let callToActionButtonTitle: String
    let onCallToActionButtonTap: () -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        VerticalCenteredScrollView(showsIndicators: false) {
            VStack(spacing: 0) {
                if horizontalSizeClass == .regular {
                    Spacer()
                        .layoutPriority(1)
                }

                VStack(spacing: 0) {
                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    Text(title)
                        .font(.title).bold()
                        .foregroundColor(.newPrimaryText)
                        .multilineTextAlignment(.center)
                        .padding(.vertical)

                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    trackCard
                        .padding(.vertical)

                    Text(subtitle)
                        .font(.footnote)
                        .foregroundColor(.newSecondaryText)
                        .multilineTextAlignment(.center)

                    if horizontalSizeClass == .compact {
                        Spacer()
                            .layoutPriority(1)
                    }

                    Button(
                        callToActionButtonTitle,
                        action: {
                            FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                            onCallToActionButtonTap()
                        }
                    )
                    .buttonStyle(.primary)
                    .shineEffect()
                    .padding(.top)
                }

                if horizontalSizeClass == .regular {
                    Spacer()
                        .layoutPriority(1)
                }
            }
            .padding()
            .frame(maxWidth: appearance.maxWidth)
        }
    }

    private var trackCard: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            if let trackImageResource {
                Image(trackImageResource)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: .infinity)
                    .frame(height: appearance.trackImageMaxHeight)
            }

            Text(trackTitle)
                .font(.title).bold()
                .foregroundColor(.newPrimaryText)
                .multilineTextAlignment(.center)

            LatexView(
                text: trackDescription,
                configuration: .init(
                    appearance: ProcessedContentView.Appearance(
                        labelFont: .preferredFont(forTextStyle: .body),
                        labelTextColor: .newSecondaryText,
                        labelTextAlignment: .center,
                        backgroundColor: .clear
                    )
                )
            )
        }
        .padding()
        .background(Color.systemSecondaryGroupedBackground)
        .addBorder(color: .clear, width: 0, cornerRadius: 24)
    }
}

#if DEBUG
#Preview {
    WelcomeOnboardingTrackDetailsContentView(
        title: "Here’s the course for you!",
        trackImageResource: .welcomeOnboardingTrackDetailsJava,
        trackTitle: "Java Developer",
        trackDescription: """
Top choice for backend systems and Android apps. Master the language that powers millions of devices worldwide!
""",
        subtitle: "You can always change this later",
        callToActionButtonTitle: "Continue",
        onCallToActionButtonTap: {}
    )
    .background(Color.systemGroupedBackground)
}
#endif
