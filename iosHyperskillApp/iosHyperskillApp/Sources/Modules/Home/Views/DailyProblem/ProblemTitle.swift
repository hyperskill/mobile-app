import SwiftUI

extension ProblemTitle {
    struct Appearance {
        let arrowSize: CGFloat = 32
    }
}

struct ProblemTitle: View {
    private(set) var appearance = Appearance()

    let state: ProblemState

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Image(state.titleIcon)
                .renderingMode(.original)
                .resizable()
                .frame(widthHeight: state.titleIconSize)

            Text(state.titleText)
                .font(.title3)
                .foregroundColor(.primaryText)

            Spacer()

            Button(
                action: {},
                label: {
                    Image(state.arrow)
                        .renderingMode(.original)
                        .resizable()
                        .frame(widthHeight: appearance.arrowSize)
                }
            )
            .disabled(state == .unavailable)
        }
    }
}

extension ProblemState {
    var titleIcon: String {
        switch self {
        case .completed:
            return Images.Home.DailyProblem.done
        case .uncompleted, .unavailable:
            return Images.Home.DailyProblem.calendar
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
            return Strings.DailyProblem.titleCompleted
        case .uncompleted, .unavailable:
            return Strings.DailyProblem.titleUncompleted
        }
    }

    var arrow: String {
        switch self {
        case .completed:
            return Images.Home.DailyProblem.arrowCompleted
        case .uncompleted, .unavailable:
            return Images.Home.DailyProblem.arrowUncompleted
        }
    }
}

struct ProblemTitle_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProblemTitle(state: .unavailable)

            ProblemTitle(state: .uncompleted)

            ProblemTitle(state: .completed)
        }
        .previewLayout(.sizeThatFits)
    }
}
