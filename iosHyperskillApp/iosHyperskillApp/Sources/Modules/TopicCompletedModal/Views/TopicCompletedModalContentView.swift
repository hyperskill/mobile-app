import shared
import SwiftUI

struct TopicCompletedModalContentView: View {
    let title: String
    let description: String
    let callToActionButtonTitle: String
    let spacebotAvatarVariantIndex: Int
    let backgroundAnimationStyle: TopicCompletedModalFeature.ViewStateBackgroundAnimationStyle

    let onCloseButtonTap: () -> Void
    let onCallToActionButtonTap: () -> Void

    var body: some View {
        ZStack(alignment: Alignment(horizontal: .trailing, vertical: .top)) {
            TopicCompletedModalBackgroundView(
                style: backgroundAnimationStyle
            )

            Button(
                action: onCloseButtonTap,
                label: {
                    Image(systemName: "xmark")
                        .padding(.horizontal)
                }
            )
            .foregroundColor(.newSecondaryText)
            .padding(.trailing)

            VStack(alignment: .center, spacing: LayoutInsets.defaultInset * 2) {
                Spacer()

                TopicCompletedModalSpacebotAvatarView(
                    spacebotAvatarVariantIndex: spacebotAvatarVariantIndex
                )

                VStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
                    Text(title)
                        .foregroundColor(.newPrimaryText)
                        .font(.title2.bold())

                    Text(description)
                        .foregroundColor(.newPrimaryText)
                        .font(.headline)
                }
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity, alignment: .center)

                Spacer()

                Button(
                    callToActionButtonTitle,
                    action: {
                        FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                        onCallToActionButtonTap()
                    }
                )
                .buttonStyle(.primary)
                .shineEffect()
            }
            .padding()
        }
    }
}

#if DEBUG
#Preview {
    TopicCompletedModalContentView(
        title: "Introduction to Kotlin",
        description: "Education is a journey, not a destination—enjoy each step towards becoming wiser.",
        callToActionButtonTitle: "Continue with next topic",
        spacebotAvatarVariantIndex: 1,
        backgroundAnimationStyle: .first,
        onCloseButtonTap: {},
        onCallToActionButtonTap: {}
    )
}
#endif
