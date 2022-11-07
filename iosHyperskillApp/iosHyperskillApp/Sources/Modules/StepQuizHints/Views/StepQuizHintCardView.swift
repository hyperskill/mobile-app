import shared
import SwiftUI

extension StepQuizHintCardView {
    struct Appearance {
        let authorAvatarWidthHeight: CGFloat = 20
        let reactionImageWidthHeight: CGFloat = 16
    }
}

struct StepQuizHintCardView: View {
    private(set) var appearance = Appearance()

    private static let shortHintTextLength = 48

    let authorAvatarSource: String?

    let authorName: String

    let hintText: String

    let hintHasReaction: Bool

    let onHintReactionButtonTap: (ReactionType) -> Void

    let onHintReportButtonTap: () -> Void

    let onHintReportModalAppear: () -> Void

    let onHintReportConfirmationButtonTap: () -> Void

    let onHintReportCancelingButtonTap: () -> Void

    let onNextHintButtonTap: (() -> Void)?

    @State private var isPresentingReportAlert = false

    @State private var showingMore = false

    private var displaingShortHintText: Bool {
        !showingMore && hintText.count > Self.shortHintTextLength
    }

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            HStack {
                LazyAvatarView(authorAvatarSource)
                    .frame(widthHeight: appearance.authorAvatarWidthHeight)

                Text(authorName)
                    .font(.caption)
                    .foregroundColor(.disabledText)

                Spacer()

                if !hintHasReaction {
                    Button(Strings.StepQuiz.Hints.reportButton) {
                        isPresentingReportAlert = true
                        onHintReportButtonTap()
                        onHintReportModalAppear()
                    }
                    .font(.subheadline)
                    .foregroundColor(Color(ColorPalette.primaryAlpha60))
                }
            }

            Text(
                displaingShortHintText
                ? "\(hintText.prefix(Self.shortHintTextLength))..."
                : hintText
            )
            .font(.subheadline)
            .foregroundColor(.primary)

            if displaingShortHintText {
                Button(Strings.StepQuiz.Hints.showMore) {
                    showingMore = true
                }
                .font(.subheadline)
                .foregroundColor(Color(ColorPalette.primary))
            }

            if hintHasReaction {
                if let onNextHintButtonTap = onNextHintButtonTap {
                    StepQuizShowHintButton(text: Strings.StepQuiz.Hints.seeNextHint) {
                        showingMore = false
                        onNextHintButtonTap()
                    }
                } else {
                    Text(Strings.StepQuiz.Hints.lastHint)
                        .font(.caption)
                        .foregroundColor(.secondaryText)
                }
            } else {
                HStack(spacing: LayoutInsets.smallInset) {
                    Text(Strings.StepQuiz.Hints.helpfulQuestion)
                        .font(.caption)
                        .foregroundColor(.secondaryText)

                    Spacer()

                    StepQuizHintReactionButtonView(
                        reactionImage: Images.StepQuiz.Hints.unhelpfulReaction,
                        reactionText: Strings.General.no,
                        onReactionButtonTap: { onHintReactionButtonTap(ReactionType.unhelpful) }
                    )

                    StepQuizHintReactionButtonView(
                        reactionImage: Images.StepQuiz.Hints.helpfulReaction,
                        reactionText: Strings.General.yes,
                        onReactionButtonTap: { onHintReactionButtonTap(ReactionType.helpful) }
                    )
                }
            }
        }
        .padding()
        .background(Color.background)
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha9))
        .alert(isPresented: $isPresentingReportAlert) {
            Alert(
                title: Text(Strings.StepQuiz.Hints.reportAlertTitle),
                message: Text(Strings.StepQuiz.Hints.reportAlertText),
                primaryButton: .default(
                    Text(Strings.General.no),
                    action: onHintReportCancelingButtonTap
                ),
                secondaryButton: .default(
                    Text(Strings.General.yes),
                    action: onHintReportConfirmationButtonTap
                )
            )
        }
    }
}

struct StepQuizHintCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizHintCardView(
                authorAvatarSource: nil,
                authorName: "Name Surname",
                hintText: "Python is used for almost everything in programming.",
                hintHasReaction: true,
                onHintReactionButtonTap: { _ in },
                onHintReportButtonTap: {},
                onHintReportModalAppear: {},
                onHintReportConfirmationButtonTap: {},
                onHintReportCancelingButtonTap: {},
                onNextHintButtonTap: {}
            )

            StepQuizHintCardView(
                authorAvatarSource: nil,
                authorName: "Name Surname",
                hintText: "Python is used for almost everything in programming.",
                hintHasReaction: false,
                onHintReactionButtonTap: { _ in },
                onHintReportButtonTap: {},
                onHintReportModalAppear: {},
                onHintReportConfirmationButtonTap: {},
                onHintReportCancelingButtonTap: {},
                onNextHintButtonTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
