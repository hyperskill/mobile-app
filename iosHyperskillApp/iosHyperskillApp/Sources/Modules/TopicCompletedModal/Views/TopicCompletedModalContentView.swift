import shared
import SwiftUI

extension TopicCompletedModalContentView {
    enum Animation {
        static let animateAvatarDelay: TimeInterval = 0.1
        static let animateAvatarDuration: TimeInterval = 0.4

        static let animateTitleDelay: TimeInterval = 0.3
        static let animateTitleDuration: TimeInterval = 0.3
        static let animateTitleHackerDuration: CGFloat = 1.3
        static let animateTitleHackerSpeed: CGFloat = 0.1

        static let animateDescriptionDelay: TimeInterval = 0.3
        static let animateDescriptionDuration: TimeInterval = 0.3
        static let animateDescriptionHackerDuration: CGFloat = 2.3
        static let animateDescriptionHackerSpeed: CGFloat = 0.08

        static let animateEarnedGemsDelay: TimeInterval = 0.1
        static let animateEarnedGemsDuration: TimeInterval = 0.3

        static let animateCallToActionButtonDelay: TimeInterval = 0.1
        static let animateCallToActionButtonDuration: TimeInterval = 0.3
    }
}

struct TopicCompletedModalContentView: View {
    private static let gemsBadgeWidthHeight: CGFloat = 28

    let title: String
    let description: String
    let earnedGemsText: String
    let callToActionButtonTitle: String
    let spacebotAvatarVariantIndex: Int
    let backgroundAnimationStyle: TopicCompletedModalFeature.ViewStateBackgroundAnimationStyle

    let onCloseButtonTap: () -> Void
    let onCallToActionButtonTap: () -> Void

    @State private var animateAvatar = false
    @State private var animateTitle = false
    @State private var animateDescription = false
    @State private var animateEarnedGems = false
    @State private var animateCallToActionButton = false

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
                .opacity(animateAvatar ? 1 : 0)

                VStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
                    if #available(iOS 16.1, *) {
                        HackerTextView(
                            text: title,
                            duration: Animation.animateTitleHackerDuration,
                            speed: Animation.animateTitleHackerSpeed
                        )
                        .font(.title2.bold())
                        .opacity(animateTitle ? 1 : 0)

                        HackerTextView(
                            text: description,
                            duration: Animation.animateDescriptionHackerDuration,
                            speed: Animation.animateDescriptionHackerSpeed
                        )
                        .font(.headline)
                        .opacity(animateDescription ? 1 : 0)
                    } else {
                        Text(title)
                            .font(.title2.bold())
                            .opacity(animateTitle ? 1 : 0)

                        Text(description)
                            .font(.headline)
                            .opacity(animateDescription ? 1 : 0)
                    }
                }
                .foregroundColor(.newPrimaryText)
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity, alignment: .center)

                HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                    Text(earnedGemsText)
                        .foregroundColor(.newSecondaryText)
                        .font(.subheadline)

                    Image(.problemOfDaySolvedModalGemsBadge)
                        .resizable()
                        .renderingMode(.original)
                        .scaledToFit()
                        .frame(widthHeight: Self.gemsBadgeWidthHeight)
                }
                .opacity(animateEarnedGems ? 1 : 0)

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
                .opacity(animateCallToActionButton ? 1 : 0)
            }
            .padding()
        }
        .taskCompatibility {
            guard !animateAvatar else {
                return
            }

            withAnimation(
                .easeInOut(duration: Animation.animateAvatarDuration)
                .delay(Animation.animateAvatarDelay)
            ) {
                animateAvatar = true
            }

            withAnimation(
                .easeInOut(duration: Animation.animateTitleDuration)
                .delay(Animation.animateTitleDelay)
            ) {
                animateTitle = true
            }

            withAnimation(
                .easeInOut(duration: Animation.animateDescriptionDuration)
                .delay(Animation.animateDescriptionDelay)
            ) {
                animateDescription = true
            }

            withAnimation(
                .easeInOut(duration: Animation.animateEarnedGemsDuration)
                .delay(Animation.animateEarnedGemsDelay)
            ) {
                animateEarnedGems = true
            }

            withAnimation(
                .easeInOut(duration: Animation.animateCallToActionButtonDuration)
                .delay(Animation.animateCallToActionButtonDelay)
            ) {
                animateCallToActionButton = true
            }
        }
    }
}

#if DEBUG
#Preview {
    TopicCompletedModalContentView(
        title: "Introduction to Kotlin",
        description: "Education is a journey, not a destination—enjoy each step towards becoming wiser.",
        earnedGemsText: "+ 15 gems",
        callToActionButtonTitle: "Continue with next topic",
        spacebotAvatarVariantIndex: 1,
        backgroundAnimationStyle: .first,
        onCloseButtonTap: {},
        onCallToActionButtonTap: {}
    )
}
#endif
