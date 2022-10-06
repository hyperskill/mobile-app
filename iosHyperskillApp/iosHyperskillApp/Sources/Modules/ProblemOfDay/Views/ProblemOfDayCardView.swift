import SwiftUI

extension ProblemOfDayCardView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let titleIconSizeDefault: CGFloat = 24
        let titleIconSizeSmall: CGFloat = 16

        let gemboxImageWidthHeight: CGFloat = 24

        let nextProblemInTextSpacing: CGFloat = 4

        let opacityUnavailable: Double = 0.38
        let opacityNormal: Double = 1

        let backgroundImageMaxHeight: Double = 116

        let shadowColor = Color.black.opacity(0.05)
        let shadowRadius: CGFloat = 8
        let shadowX: CGFloat = 0
        let shadowY: CGFloat = 2
    }
}

struct ProblemOfDayCardView: View {
    private(set) var appearance = Appearance()

    let viewModel: ProblemOfDayViewModel

    var body: some View {
        let viewData = viewModel.makeViewData()

        Button(
            action: {
                viewModel.doStepPresentation(stepID: viewData.stepID)
            },
            label: {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    ProblemOfDayTitle(
                        appearance: .init(
                            titleIconSize: viewData.state == .completed
                                ? appearance.titleIconSizeSmall
                                : appearance.titleIconSizeDefault
                        ),
                        titleIcon: viewData.state.titleIconName,
                        titleText: viewData.state.titleText,
                        arrowIcon: viewData.state.titleArrowIconName
                    )

                    Text(viewData.state.descriptionText)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    if viewData.state == .uncompleted,
                       let timeToSolve = viewData.timeToSolve {
                        buildTimeToSolve(timeToSolve: timeToSolve)
                    }

                    if viewData.state != .unavailable,
                       let nextProblemIn = viewData.nextProblemIn {
                        buildNextProblemIn(nextProblemIn: nextProblemIn, needToRefresh: viewData.needToRefresh)
                    }
                }
                .padding()
                .opacity(viewData.state == .unavailable ? appearance.opacityUnavailable : appearance.opacityNormal)
                .background(
                    Image(viewData.state.hexogensImageName)
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(maxHeight: appearance.backgroundImageMaxHeight)
                    ,
                    alignment: .topTrailing
                )
                .background(Color(ColorPalette.surface))
                .addBorder()
                .shadow(
                    color: appearance.shadowColor,
                    radius: appearance.shadowRadius,
                    x: appearance.shadowX,
                    y: appearance.shadowY
                )
            }
        )
        .buttonStyle(BounceButtonStyle())
        .disabled(viewData.stepID == nil)
    }

    @ViewBuilder
    private func buildTimeToSolve(timeToSolve: String) -> some View {
        HStack {
            Text(timeToSolve)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            Spacer()

            Image(Images.Home.ProblemOfDay.gembox)
                .renderingMode(.original)
                .resizable()
                .frame(widthHeight: appearance.gemboxImageWidthHeight)
        }
    }

    @ViewBuilder
    private func buildNextProblemIn(nextProblemIn: String, needToRefresh: Bool) -> some View {
        Divider()

        if needToRefresh {
            Button(
                Strings.Placeholder.networkErrorButtonText,
                action: viewModel.doReloadAction
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        } else {
            HStack(spacing: appearance.nextProblemInTextSpacing) {
                Text(Strings.ProblemOfDay.nextProblemIn)
                    .font(.body)
                    .foregroundColor(.primaryText)

                Text(nextProblemIn)
                    .font(.headline)
                    .foregroundColor(.primaryText)
            }
        }
    }

    // MARK: - Inner Types -

    enum State {
        case completed
        case uncompleted
        case unavailable

        fileprivate var descriptionText: String {
            switch self {
            case .completed:
                return Strings.ProblemOfDay.getBack
            case .uncompleted:
                return Strings.ProblemOfDay.solveARandomProblem
            case .unavailable:
                return Strings.ProblemOfDay.noProblemsToSolve
            }
        }

        fileprivate var hexogensImageName: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.hexogensCompleted
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.hexogensUncompleted
            }
        }

        fileprivate var titleIconName: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.done
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.calendar
            }
        }

        fileprivate var titleText: String {
            switch self {
            case .completed:
                return Strings.ProblemOfDay.titleCompleted
            case .uncompleted, .unavailable:
                return Strings.ProblemOfDay.titleUncompleted
            }
        }

        fileprivate var titleArrowIconName: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.arrowCompleted
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.arrowUncompleted
            }
        }
    }
}

// MARK: - Previews -

#if DEBUG
struct DailyProblemCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProblemOfDayAssembly
                .makePlaceholder(state: .uncompleted)
                .makeModule()

            ProblemOfDayAssembly
                .makePlaceholder(
                    state: .uncompleted,
                    secondsToComplete: 112,
                    nextProblemIn: 7260
                )
                .makeModule()

            ProblemOfDayAssembly
                .makePlaceholder(
                    state: .uncompleted,
                    nextProblemIn: 59
                )
                .makeModule()

            ProblemOfDayAssembly
                .makePlaceholder(state: .unavailable)
                .makeModule()

            ProblemOfDayAssembly
                .makePlaceholder(state: .completed)
                .makeModule()
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
#endif
