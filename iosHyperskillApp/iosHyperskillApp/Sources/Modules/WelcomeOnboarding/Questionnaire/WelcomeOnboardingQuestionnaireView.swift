import shared
import SwiftUI

extension WelcomeOnboardingQuestionnaireView {
    enum Appearance {
        static let spacing = LayoutInsets.defaultInset

        static let iconSize: CGFloat = 16
    }
}

struct WelcomeOnboardingQuestionnaireView: View {
    let viewState: WelcomeQuestionnaireViewState

    let onViewDidAppear: () -> Void
    let onQuestionnaireItemTap: (WelcomeQuestionnaireItem) -> Void

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: onViewDidAppear)

            BackgroundView(color: Color.systemGroupedBackground)

            ScrollView {
                VStack(spacing: Appearance.spacing * 2) {
                    Text(viewState.title)
                        .font(.title).bold()
                        .foregroundColor(.newPrimaryText)
                        .multilineTextAlignment(.center)

                    list
                }
                .padding()
            }
            .scrollBounceBehaviorBasedOnSize()
        }
    }

    @MainActor private var list: some View {
        VStack(spacing: Appearance.spacing) {
            ForEach(viewState.items, id: \.text) { item in
                listItem(item)
            }
        }
    }

    @MainActor
    private func listItem(_ item: WelcomeQuestionnaireItem) -> some View {
        Button(
            action: {
                FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                onQuestionnaireItemTap(item)
            },
            label: {
                HStack(alignment: .center, spacing: Appearance.spacing) {
                    if let imageResource = item.type.imageResource {
                        Image(imageResource)
                            .renderingMode(.original)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: Appearance.iconSize)
                    }

                    Text(item.text)
                        .font(.body)
                        .foregroundColor(.newPrimaryText)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
                .background(Color.systemSecondaryGroupedBackground)
                .addBorder(color: .clear, width: 0)
            }
        )
        .buttonStyle(BounceButtonStyle())
    }
}

#if DEBUG
#Preview {
    WelcomeOnboardingQuestionnaireView(
        viewState: WelcomeQuestionnaireViewState(
            title: "How did you hear about Hyperskill?",
            items: [
                WelcomeQuestionnaireItem(
                    type: WelcomeQuestionnaireItemTypeClientSource.tikTok,
                    text: "TikTok"
                ),
                WelcomeQuestionnaireItem(
                    type: WelcomeQuestionnaireItemTypeClientSource.googleSearch,
                    text: "Google Search"
                ),
                WelcomeQuestionnaireItem(
                    type: WelcomeQuestionnaireItemTypeClientSource.appStore,
                    text: "App Store"
                )
            ]
        ),
        onViewDidAppear: {},
        onQuestionnaireItemTap: { _ in }
    )
}
#endif
