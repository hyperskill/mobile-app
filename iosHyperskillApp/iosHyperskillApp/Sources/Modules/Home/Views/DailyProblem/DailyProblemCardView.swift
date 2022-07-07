import SwiftUI

extension DailyProblemCardView {
    struct Appearance {
        let spacing: CGFloat = 12
        let gemboxSize: CGFloat = 24
        let unavailableOpacity: Double = 0.38
        let normalOpacity: Double = 1
    }
}

enum ProblemState {
    case completed
    case uncompleted
    case unavailable
}

struct DailyProblemCardView: View {
    private(set) var appearance = Appearance()

    let state: ProblemState

    let timeToSolve: String?

    let nextProblemIn: String?

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProblemTitle(state: state)

            Text(state.desc)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            if let timeToSolve = timeToSolve {
                HStack {
                    Text(timeToSolve)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    Spacer()

                    Image(Images.Home.DailyProblem.gembox)
                        .renderingMode(.original)
                        .resizable()
                        .frame(widthHeight: appearance.gemboxSize)
                }

                Divider()
            }

            if let nextProblemIn = nextProblemIn, state != .unavailable {
                HStack {
                    Text(Strings.DailyProblem.nextProblemIn)
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
                .renderingMode(.original),
            alignment: .topTrailing
        )
        .addBorder(
            color: Color(ColorPalette.onSurfaceAlpha12)
        )
    }
}

extension ProblemState {
    var desc: String {
        switch self {
        case .completed:
            return Strings.DailyProblem.getBack
        case .uncompleted:
            return Strings.DailyProblem.solveARandomProblem
        case .unavailable:
            return Strings.DailyProblem.noProblemsToSolve
        }
    }

    var hexogens: String {
        switch self {
        case .completed:
            return Images.Home.DailyProblem.hexogensCompleted
        case .uncompleted, .unavailable:
            return Images.Home.DailyProblem.hexogensUncompleted
        }
    }
}

struct DailyProblemCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            DailyProblemCardView(state: .uncompleted, timeToSolve: "5 minutes", nextProblemIn: "6 hrs 27 mins")

            DailyProblemCardView(state: .unavailable, timeToSolve: nil, nextProblemIn: nil)

            DailyProblemCardView(state: .completed, timeToSolve: nil, nextProblemIn: "6 hrs 27 mins")
        }
        .previewLayout(.sizeThatFits)
    }
}
