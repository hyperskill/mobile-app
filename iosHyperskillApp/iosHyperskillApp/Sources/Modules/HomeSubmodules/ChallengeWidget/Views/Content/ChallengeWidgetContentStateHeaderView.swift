import shared
import SwiftUI

struct ChallengeWidgetContentStateHeaderView: View {
    let title: String
    let badgeText: String
    let imageResource: ImageResource

    var body: some View {
        HStack(alignment: .center, spacing: 0) {
            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.headline)
                    .foregroundColor(.primaryText)

                BadgeView(text: badgeText, style: .blue)
            }

            Spacer(minLength: LayoutInsets.defaultInset)

            Image(imageResource)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: 54, maxHeight: 48)
        }
    }
}

extension ChallengeWidgetContentStateHeaderView {
    init(stateKs: ChallengeWidgetViewStateContentKs) {
        let headerData = stateKs.sealed.headerData

        let imageResource: ImageResource = switch stateKs {
        case .announcement:
            .challengeWidgetStatusAnnouncement
        case .completed:
            .challengeWidgetStatusCompleted
        case .ended:
            .challengeWidgetStatusEnded
        case .happeningNow:
            .challengeWidgetStatusHappeningNow
        case .partiallyCompleted:
            .challengeWidgetStatusPartiallyCompleted
        }

        self.init(
            title: headerData.title,
            badgeText: headerData.formattedDurationOfTime,
            imageResource: imageResource
        )
    }
}

#if DEBUG
#Preview {
    let title = "Advent Streak Challenge"
    let badgeText = "6 Oct - 12 Oct"

    return VStack {
        ChallengeWidgetContentStateHeaderView(
            title: title,
            badgeText: badgeText,
            imageResource: .challengeWidgetStatusAnnouncement
        )

        ChallengeWidgetContentStateHeaderView(
            title: title,
            badgeText: badgeText,
            imageResource: .challengeWidgetStatusCompleted
        )

        ChallengeWidgetContentStateHeaderView(
            title: title,
            badgeText: badgeText,
            imageResource: .challengeWidgetStatusEnded
        )

        ChallengeWidgetContentStateHeaderView(
            title: title,
            badgeText: badgeText,
            imageResource: .challengeWidgetStatusHappeningNow
        )

        ChallengeWidgetContentStateHeaderView(
            title: title,
            badgeText: badgeText,
            imageResource: .challengeWidgetStatusPartiallyCompleted
        )
    }
    .padding()
}
#endif
