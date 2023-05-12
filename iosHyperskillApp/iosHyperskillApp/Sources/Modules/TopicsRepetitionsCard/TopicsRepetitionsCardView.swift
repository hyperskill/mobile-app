import SwiftUI

extension TopicsRepetitionsCardView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let backgroundImageMaxHeight: Double = 116

        let arrowIconSize: CGFloat = 32
    }
}

struct TopicsRepetitionsCardView: View {
    private(set) var appearance = Appearance()

    let topicsToRepeatCount: Int

    let onTap: () -> Void

    let isFreemiumEnabled: Bool

    var body: some View {
        let state: State = topicsToRepeatCount > 0 ? .uncompleted : .completed

        Button(
            action: onTap,
            label: {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    HStack(spacing: LayoutInsets.smallInset) {
                        Text(state.titleText)
                            .font(.title3)
                            .foregroundColor(.primaryText)

                        Spacer()

                        Image(state.titleArrowIconName)
                            .renderingMode(.original)
                            .resizable()
                            .frame(widthHeight: appearance.arrowIconSize)
                    }

                    state.buildFooter(topicsToRepeatCount: topicsToRepeatCount)

                    if isFreemiumEnabled && state == .uncompleted {
                        BadgeView.freemiumRepeatUnlimited()
                    }
                }
                .padding()
                .background(
                    Image(state.hexogensImageName)
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(maxHeight: appearance.backgroundImageMaxHeight)
                    ,
                    alignment: .topTrailing
                )
                .background(Color(ColorPalette.surface))
                .addBorder()
            }
        )
        .buttonStyle(BounceButtonStyle())
    }

    // MARK: - Inner Types -

    enum State {
        case completed
        case uncompleted

        fileprivate var titleArrowIconName: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.arrowCompleted
            case .uncompleted:
                return Images.Home.ProblemOfDay.arrowUncompleted
            }
        }

        fileprivate var hexogensImageName: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.hexogensCompleted
            case .uncompleted:
                return Images.Home.ProblemOfDay.hexogensUncompleted
            }
        }

        fileprivate var titleText: String {
            switch self {
            case .completed:
                return Strings.General.goodJob
            case .uncompleted:
                return Strings.TopicsRepetitions.Card.titleUncompleted
            }
        }

        @ViewBuilder
        func buildFooter(topicsToRepeatCount: Int) -> some View {
            switch self {
            case .completed:
                Text(Strings.TopicsRepetitions.Card.textCompleted)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)
            case .uncompleted:
                TopicsRepetitionsCountView(topicsToRepeatCount: topicsToRepeatCount)
            }
        }
    }
}

struct TopicsRepetitionsCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TopicsRepetitionsCardView(
                topicsToRepeatCount: 4,
                onTap: {},
                isFreemiumEnabled: true
            )

            TopicsRepetitionsCardView(
                topicsToRepeatCount: 0,
                onTap: {},
                isFreemiumEnabled: true
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
