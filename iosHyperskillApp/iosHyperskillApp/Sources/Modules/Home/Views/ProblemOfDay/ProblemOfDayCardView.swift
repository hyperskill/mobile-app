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

    let state: ProblemOfDayState

    let timeToSolve: String?

    let nextProblemIn: String?

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProblemOfDayTitle(
                appearance: .init(
                    titleIconSize: state == .completed
                    ? appearance.titleIconSizeSmall
                    : appearance.titleIconSizeDefault
                ),
                titleIcon: state.titleIcon,
                titleText: state.titleText,
                arrowIcon: state.arrowIcon,
                arrowDisabled: state == .unavailable
            )

            Text(state.desc)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            if let timeToSolve = timeToSolve {
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

                Divider()
            }

            if let nextProblemIn = nextProblemIn, state != .unavailable {
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
        .padding()
        .opacity(state == .unavailable ? appearance.unavailableOpacity : appearance.normalOpacity)
        .background(
            Image(state.hexogens)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxHeight: appearance.backgroundMaxHeight)
            ,
            alignment: .topTrailing
        )
        .addBorder(
            color: Color(ColorPalette.onSurfaceAlpha12)
        )
    }

    // MARK: - ProblemOfDayState -

    enum ProblemOfDayState {
        case completed
        case uncompleted
        case unavailable

        var desc: String {
            switch self {
            case .completed:
                return Strings.ProblemOfDay.getBack
            case .uncompleted:
                return Strings.ProblemOfDay.solveARandomProblem
            case .unavailable:
                return Strings.ProblemOfDay.noProblemsToSolve
            }
        }

        var hexogens: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.hexogensCompleted
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.hexogensUncompleted
            }
        }

        var titleIcon: String {
            switch self {
            case .completed:
                return Images.Home.ProblemOfDay.done
            case .uncompleted, .unavailable:
                return Images.Home.ProblemOfDay.calendar
            }
        }

        var titleIconSize: CGFloat {
            switch self {
            case .completed:
                return 16
            case .uncompleted, .unavailable:
                return 24
            }
        }

        var titleText: String {
            switch self {
            case .completed:
                return Strings.ProblemOfDay.titleCompleted
            case .uncompleted, .unavailable:
                return Strings.ProblemOfDay.titleUncompleted
            }
        }

        var arrowIcon: String {
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
            ProblemOfDayCardView(state: .uncompleted, timeToSolve: "5 minutes", nextProblemIn: "6 hrs 27 mins")

            ProblemOfDayCardView(state: .unavailable, timeToSolve: nil, nextProblemIn: nil)

            ProblemOfDayCardView(state: .completed, timeToSolve: nil, nextProblemIn: "6 hrs 27 mins")
        }
        .previewLayout(.sizeThatFits)
    }
}
