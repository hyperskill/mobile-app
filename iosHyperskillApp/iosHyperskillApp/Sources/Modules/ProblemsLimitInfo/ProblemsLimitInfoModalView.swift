import shared
import SwiftUI

extension ProblemsLimitInfoModalView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let animationViewWidthHeight: CGFloat = 109
    }
}

struct ProblemsLimitInfoModalView: View {
    private(set) var appearance = Appearance()

    let viewState: ProblemsLimitInfoModalFeature.ViewState

    let onCallToActionButtonTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text(viewState.title)
                    .foregroundColor(.newPrimaryText)
                    .font(.title2.bold())

                if let limitsDescription = viewState.limitsDescription {
                    Text(limitsDescription)
                        .foregroundColor(.newSecondaryText)
                        .font(.subheadline)
                }
            }

            VStack(alignment: .center, spacing: appearance.interitemSpacing) {
                animationView

                if let leftLimitsText = viewState.leftLimitsText {
                    Text(leftLimitsText)
                        .foregroundColor(.newPrimaryText)
                        .font(.headline)
                }

                if let resetInText = viewState.resetInText {
                    Text(resetInText)
                        .foregroundColor(.newSecondaryText)
                        .font(.subheadline)
                }
            }
            .frame(maxWidth: .infinity, alignment: .center)
            .padding(.vertical)

            if let unlockDescription = viewState.unlockDescription {
                Text(unlockDescription)
                    .foregroundColor(.newSecondaryText)
                    .font(.subheadline)
                    .multilineTextAlignment(.center)
            }

            Button(
                viewState.buttonText,
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

    private var animationView: some View {
        let fileName: LottieAnimationFileName? = {
            switch viewState.animation {
            case .fullLimits:
                LottieAnimations.problemsLimitInfoModalFullLimits
            case .partiallySpentLimits:
                LottieAnimations.problemsLimitInfoModalPartiallyFilled
            case .noLimitsLeft:
                LottieAnimations.problemsLimitInfoModalNoLimits
            default:
                nil
            }
        }()

        return LottieAnimationViewWrapper(
            fileName: fileName.require(),
            loopMode: viewState.animation.isLooped ? .loop : .playOnce,
            animationSpeed: viewState.animation == .fullLimits ? 0.25 : 1
        )
        .frame(widthHeight: appearance.animationViewWidthHeight)
    }
}

#if DEBUG
#Preview("fullLimits") {
    ProblemsLimitInfoModalView(
        viewState: ProblemsLimitInfoModalFeature.ViewState(
            title: "Out of Limits/Lives: Need a Recharge?",
            limitsDescription: "Limits refresh daily and get deducted when you solve a problem.",
            animation: ProblemsLimitInfoModalFeature.ViewStateAnimation.fullLimits,
            leftLimitsText: "0 problems/lives left",
            resetInText: "Reset in X h",
            unlockDescription: "Fast learner? Unlock unlimited problems with Mobile only plan",
            buttonText: "Unlock unlimited problems"
        ),
        onCallToActionButtonTap: {}
    )
}

#Preview("partiallySpentLimits") {
    ProblemsLimitInfoModalView(
        viewState: ProblemsLimitInfoModalFeature.ViewState(
            title: "Out of Limits/Lives: Need a Recharge?",
            limitsDescription: "Limits refresh daily and get deducted when you solve a problem.",
            animation: ProblemsLimitInfoModalFeature.ViewStateAnimation.partiallySpentLimits,
            leftLimitsText: "0 problems/lives left",
            resetInText: "Reset in X h",
            unlockDescription: "Fast learner? Unlock unlimited problems with Mobile only plan",
            buttonText: "Unlock unlimited problems"
        ),
        onCallToActionButtonTap: {}
    )
}

#Preview("noLimitsLeft") {
    ProblemsLimitInfoModalView(
        viewState: ProblemsLimitInfoModalFeature.ViewState(
            title: "Out of Limits/Lives: Need a Recharge?",
            limitsDescription: "Limits refresh daily and get deducted when you solve a problem.",
            animation: ProblemsLimitInfoModalFeature.ViewStateAnimation.noLimitsLeft,
            leftLimitsText: "0 problems/lives left",
            resetInText: "Reset in X h",
            unlockDescription: "Fast learner? Unlock unlimited problems with Mobile only plan",
            buttonText: "Unlock unlimited problems"
        ),
        onCallToActionButtonTap: {}
    )
}
#endif
