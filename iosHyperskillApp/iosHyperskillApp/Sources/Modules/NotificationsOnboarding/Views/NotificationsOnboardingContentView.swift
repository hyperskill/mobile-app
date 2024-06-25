import SwiftUI

extension NotificationsOnboardingContentView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationHeight: CGFloat = 230

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct NotificationsOnboardingContentView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let formattedDailyStudyRemindersInterval: String
    let onDailyStudyRemindsIntervalButtonTap: () -> Void

    let onPrimaryButtonTap: () -> Void
    let onSecondaryButtonTap: () -> Void

    var body: some View {
        VerticalCenteredScrollView(showsIndicators: false) {
            VStack(spacing: 0) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                VStack(spacing: 0) {
                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    Text(Strings.NotificationsOnboarding.title)
                        .font(.title).bold()
                        .foregroundColor(.newPrimaryText)
                        .multilineTextAlignment(.center)

                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    illustration
                        .padding(.vertical)

                    dailyStudyRemindersInterval
                        .padding(.vertical)

                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    actionButtons
                        .padding(.top)
                }

                if horizontalSizeClass == .regular {
                    Spacer()
                }
            }
            .padding()
            .frame(maxWidth: appearance.maxWidth)
        }
    }

    private var illustration: some View {
        Image(.notificationsOnboardingIllustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(maxWidth: .infinity)
            .frame(maxHeight: appearance.illustrationHeight)
    }

    @MainActor private var dailyStudyRemindersInterval: some View {
        VStack(spacing: appearance.spacing) {
            Button(
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    onDailyStudyRemindsIntervalButtonTap()
                },
                label: {
                    HStack {
                        Text(Strings.NotificationsOnboarding.dailyStudyRemindersIntervalPrefix)
                            .foregroundColor(.newPrimaryText)

                        Text(formattedDailyStudyRemindersInterval)
                            .foregroundColor(Color(ColorPalette.newButtonPrimary))
                            .animation(.default, value: formattedDailyStudyRemindersInterval)
                    }
                    .font(.headline)
                    .padding()
                    .background(Color.systemSecondaryGroupedBackground)
                    .clipShape(Capsule())
                }
            )
            .buttonStyle(BounceButtonStyle())

            Text(Strings.NotificationsOnboarding.dailyStudyRemindersIntervalDescription)
                .font(.footnote)
                .foregroundColor(.newSecondaryText)
                .multilineTextAlignment(.center)
        }
    }

    @MainActor private var actionButtons: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Button(
                Strings.NotificationsOnboarding.buttonPrimary,
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    onPrimaryButtonTap()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()

            Button(
                Strings.NotificationsOnboarding.buttonSecondary,
                action: {
                    actionButtonsFeedbackGenerator.triggerFeedback()
                    onSecondaryButtonTap()
                }
            )
            .buttonStyle(GhostButtonStyle())
        }
    }
}

#if DEBUG
#Preview {
    NotificationsOnboardingContentView(
        formattedDailyStudyRemindersInterval: "12:00",
        onDailyStudyRemindsIntervalButtonTap: {},
        onPrimaryButtonTap: {},
        onSecondaryButtonTap: {}
    )
    .background(Color.systemGroupedBackground)
}
#endif
