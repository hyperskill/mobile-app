import shared
import SwiftUI

extension ChallengeWidgetContentStateDeadlineView {
    struct Appearance {
        static let countDownStackSpacing: CGFloat = 4
    }
}

struct ChallengeWidgetContentStateDeadlineView: View {
    let presentationContext: PresentationContext

    var body: some View {
        switch presentationContext {
        case .empty:
            EmptyView()
        case .reloadButton(let action):
            Button(
                Strings.Placeholder.networkErrorButtonText,
                action: action
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        case .text(let title, let subtitle):
            HStack(spacing: Appearance.countDownStackSpacing) {
                Text(title)
                    .font(.body)

                Text(subtitle)
                    .font(.headline)
                    .animation(.default, value: subtitle)
            }
            .foregroundColor(.primaryText)
        }
    }

    enum PresentationContext: Equatable {
        case empty
        case reloadButton(action: () -> Void)
        case text(title: String, subtitle: String)

        static func == (lhs: PresentationContext, rhs: PresentationContext) -> Bool {
            switch (lhs, rhs) {
            case (.empty, .empty):
                true
            case (.reloadButton, .reloadButton):
                true
            case (.text(let lhsTitle, let lhsSubtitle), .text(let rhsTitle, let rhsSubtitle)):
                lhsTitle == rhsTitle && lhsSubtitle == rhsSubtitle
            default:
                false
            }
        }
    }
}

extension ChallengeWidgetContentStateDeadlineView {
    init(
        startsInState: ChallengeWidgetViewStateContentAnnouncementStartsInState,
        reloadAction: @escaping () -> Void
    ) {
        switch ChallengeWidgetViewStateContentAnnouncementStartsInStateKs(startsInState) {
        case .deadline:
            self.init(presentationContext: .reloadButton(action: reloadAction))
        case .timeRemaining(let data):
            self.init(presentationContext: .text(title: data.title, subtitle: data.subtitle))
        }
    }
}

extension ChallengeWidgetContentStateDeadlineView {
    init(
        completeInState: ChallengeWidgetViewStateContentHappeningNowCompleteInState,
        reloadAction: @escaping () -> Void
    ) {
        switch ChallengeWidgetViewStateContentHappeningNowCompleteInStateKs(completeInState) {
        case .deadline:
            self.init(presentationContext: .reloadButton(action: reloadAction))
        case .timeRemaining(let data):
            self.init(presentationContext: .text(title: data.title, subtitle: data.subtitle))
        case .empty:
            self.init(presentationContext: .empty)
        }
    }
}

#Preview {
    VStack {
        ChallengeWidgetContentStateDeadlineView(presentationContext: .reloadButton(action: {}))

        ChallengeWidgetContentStateDeadlineView(
            presentationContext: .text(title: "Starts in", subtitle: "6 hrs 27 mins")
        )
    }
    .padding()
}
