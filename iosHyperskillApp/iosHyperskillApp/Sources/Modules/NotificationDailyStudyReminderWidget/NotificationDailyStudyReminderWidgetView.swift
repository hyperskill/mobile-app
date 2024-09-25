import SwiftUI

extension NotificationDailyStudyReminderWidgetView {
    struct Appearance {
        let illustrationSize = CGSize(width: 89, height: 86)

        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct NotificationDailyStudyReminderWidgetView: View {
    private(set) var appearance = Appearance()

    var onCallToAction: () -> Void
    var onClose: () -> Void
    var onViewedEvent: () -> Void

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: onViewedEvent)

            Button(
                action: onCallToAction,
                label: {
                    ZStack(alignment: Alignment(horizontal: .trailing, vertical: .top)) {
                        HStack(alignment: .center, spacing: appearance.spacing) {
                            textConent
                            illustration
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding([.leading, .vertical])

                        closeButton
                    }
                    .foregroundColor(.white)
                    .background(backgroundGradient)
                }
            )
            .buttonStyle(BounceButtonStyle())
        }
    }

    private var textConent: some View {
        VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
            Text(Strings.NotificationDailyStudyReminderWidget.title)
                .font(.headline)
            Text(Strings.NotificationDailyStudyReminderWidget.subtitle)
                .font(.subheadline)
        }
    }

    private var illustration: some View {
        Image(.usersInterviewWidgetIllustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(size: appearance.illustrationSize)
    }

    private var closeButton: some View {
        Button(
            action: onClose,
            label: {
                Image(systemName: "xmark.circle.fill")
                    .padding(.all, appearance.interitemSpacing)
            }
        )
    }

    private var backgroundGradient: some View {
        Image(.usersInterviewWidgetGradient)
            .renderingMode(.original)
            .resizable()
            .addBorder(color: .clear, width: 0)
    }
}

#if DEBUG
#Preview {
    NotificationDailyStudyReminderWidgetView(
        onCallToAction: {},
        onClose: {},
        onViewedEvent: {}
    )
    .padding()
}
#endif
