import SwiftUI

extension TopicsRepetitionsCardView {
    struct Appearance {
        let spacing: CGFloat = 12

        let backgroundImageMaxHeight: Double = 116

        let arrowIconSize: CGFloat = 32
    }
}

struct TopicsRepetitionsCardView: View {
    private(set) var appearance = Appearance()

    let topicsToRepeatCount: Int

    let onTap: () -> Void

    var body: some View {
        let state: Self.State = topicsToRepeatCount > 0 ? .uncompleted : .completed

        Button(
            action: onTap,
            label: {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    HStack(spacing: LayoutInsets.smallInset) {
                        Text(state.buildTitleText())
                            .font(.title3)
                            .foregroundColor(.primaryText)

                        Spacer()

                        Image(state.titleArrowIconName)
                            .renderingMode(.original)
                            .resizable()
                            .frame(widthHeight: appearance.arrowIconSize)
                    }

                    state.buildFooter(topicsToRepeatCount: topicsToRepeatCount)
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
        .disabled(state == .completed)
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

        func buildTitleText() -> String {
            switch self {
            case .completed:
                return Strings.TopicsRepetitions.cardTitleCompleted
            case .uncompleted:
                return Strings.TopicsRepetitions.cardTitleUncompleted
            }
        }

        @ViewBuilder
        func buildFooter(topicsToRepeatCount: Int) -> some View {
            switch self {
            case .completed:
                Text(Strings.TopicsRepetitions.cardTextCompleted)
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
            TopicsRepetitionsCardView(topicsToRepeatCount: 4, onTap: {})

            TopicsRepetitionsCardView(topicsToRepeatCount: 0, onTap: {})
        }
        .previewLayout(.sizeThatFits)
    }
}
