import SwiftUI

extension ProblemOfDayCardView {
    struct Appearance {
        let spacing: CGFloat = 12

        let gemboxSize: CGFloat = 24

        let unavailableOpacity: Double = 0.38
        let normalOpacity: Double = 1

        let backgroundMaxHeight: Double = 116

        let titleIconSizeDefault: CGFloat = 24
        let titleIconSizeSmall: CGFloat = 16
    }
}

struct ProblemOfDayCardView: View {
    private(set) var appearance = Appearance()

    let viewData: ProblemOfDayViewData

    let onReloadTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProblemOfDayTitle(
                appearance: .init(
                    titleIconSize: viewData.state == .completed
                    ? appearance.titleIconSizeSmall
                    : appearance.titleIconSizeDefault
                ),
                titleIcon: viewData.state.titleIcon,
                titleText: viewData.state.titleText,
                arrowIcon: viewData.state.arrowIcon,
                isArrowDisabled: viewData.state == .unavailable,
                onTap: {}
            )

            Text(viewData.state.desc)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            if let timeToSolve = viewData.timeToSolve, viewData.state == .uncompleted {
                HStack {
                    Text(timeToSolve)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    Spacer()

                    Image(Images.Home.ProblemOfDay.gembox)
                        .renderingMode(.original)
                        .resizable()
                        .frame(widthHeight: appearance.gemboxSize)
                }
            }

            if let nextProblemIn = viewData.nextProblemIn, viewData.state != .unavailable {
                Divider()

                if viewData.needToRefresh {
                    Button(Strings.Placeholder.networkErrorButtonText, action: onReloadTap)
                        .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                } else {
                    HStack {
                        Text(Strings.ProblemOfDay.nextProblemIn)
                            .font(.body)
                            .foregroundColor(.primaryText)

                        Text(nextProblemIn)
                            .font(.subheadline)
                            .foregroundColor(.primaryText)
                    }
                }
            }
        }
        .padding()
        .opacity(viewData.state == .unavailable ? appearance.unavailableOpacity : appearance.normalOpacity)
        .background(
            Image(viewData.state.hexogens)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxHeight: appearance.backgroundMaxHeight)
            ,
            alignment: .topTrailing
        )
        .addBorder()
    }

    // MARK: - ProblemOfDayState -

    enum ProblemOfDayState {
        case completed
        case uncompleted
        case unavailable

        fileprivate var desc: String {
            switch self {
            case .completed:
                return Strings.ProblemOfDay.getBack
            case .uncompleted:
                return Strings.ProblemOfDay.solveARandomProblem
            case .unavailable:
                return Strings.ProblemOfDay.noProblemsToSolve
            }
        }

        fileprivate var hexogens: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.hexogensCompleted
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.hexogensUncompleted
            }
        }

        fileprivate var titleIcon: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.done
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.calendar
            }
        }

        fileprivate var titleIconSize: CGFloat {
            switch self {
            case .completed:
                return 16
            case .uncompleted, .unavailable:
                return 24
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

        fileprivate var arrowIcon: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.arrowCompleted
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.arrowUncompleted
            }
        }
    }
}

struct DailyProblemCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProblemOfDayCardView(
                viewData: ProblemOfDayViewData(
                    state: .uncompleted,
                    timeToSolve: "5 minutes",
                    nextProblemIn: "6 hours 27 minutes",
                    needToRefresh: true
                ),
                onReloadTap: {}
            )

            ProblemOfDayCardView(
                viewData: ProblemOfDayViewData(
                    state: .unavailable,
                    timeToSolve: nil,
                    nextProblemIn: nil,
                    needToRefresh: false
                ),
                onReloadTap: {}
            )

            ProblemOfDayCardView(
                viewData: ProblemOfDayViewData(
                    state: .completed,
                    timeToSolve: nil,
                    nextProblemIn: "6 hours 27 minutes",
                    needToRefresh: false
                ),
                onReloadTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
