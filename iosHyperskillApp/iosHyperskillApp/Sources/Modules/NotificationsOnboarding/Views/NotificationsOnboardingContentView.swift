import SwiftUI

extension NotificationsOnboardingContentView {
    struct Appearance {
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationHeight: CGFloat = 230

        let maxWidth: CGFloat = DeviceInfo.current.isPad ? 400 : .infinity
    }
}

struct NotificationsOnboardingContentView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private let actionButtonsFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    let dailyStudyRemindersStartHour: Int
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
                    header
                        .padding(.vertical)
                    if horizontalSizeClass == .compact {
                        Spacer()
                    }

                    illustration
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

    @MainActor private var header: some View {
        VStack(alignment: .center, spacing: appearance.interitemSpacing) {
            Text(Strings.NotificationsOnboarding.title)
                .font(.title)
                .foregroundColor(.newPrimaryText)

            HStack {
                Text(Strings.NotificationsOnboarding.dailyStudyRemindersIntervalPrefix)
                    .font(.body)
                    .foregroundColor(.newPrimaryText)

                Button(
                    formattedDailyStudyRemindersInterval,
                    action: {
                        actionButtonsFeedbackGenerator.triggerFeedback()
                        onDailyStudyRemindsIntervalButtonTap()
                    }
                )
                .buttonStyle(GhostButtonStyle(maxWidth: nil))
                .animation(.default, value: formattedDailyStudyRemindersInterval)
            }
        }
        .multilineTextAlignment(.center)
    }

    private var illustration: some View {
        Image(.notificationsOnboardingIllustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(maxWidth: .infinity)
            .frame(maxHeight: appearance.illustrationHeight)
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
            .buttonStyle(RoundedRectangleButtonStyle(style: .newViolet))
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

struct NotificationsOnboardingContentView_Previews: PreviewProvider {
    static var previews: some View {
        NotificationsOnboardingContentView(
            dailyStudyRemindersStartHour: 12,
            formattedDailyStudyRemindersInterval: "12:00 – 13:00",
            onDailyStudyRemindsIntervalButtonTap: {},
            onPrimaryButtonTap: {},
            onSecondaryButtonTap: {}
        )
        .previewDevice(PreviewDevice(rawValue: "iPhone 14 Pro"))

        NotificationsOnboardingContentView(
            dailyStudyRemindersStartHour: 12,
            formattedDailyStudyRemindersInterval: "12:00 – 13:00",
            onDailyStudyRemindsIntervalButtonTap: {},
            onPrimaryButtonTap: {},
            onSecondaryButtonTap: {}
        )
        .previewDevice(PreviewDevice(rawValue: "iPad (10th generation)"))
    }
}
