import SwiftUI

extension ProblemOfDayTitle {
    struct Appearance {
        let titleIconSize: CGFloat
        let arrowIconSize: CGFloat = 32
    }
}

struct ProblemOfDayTitle: View {
    let appearance: Appearance

    let titleIcon: String
    let titleText: String

    let arrowIcon: String
    let isArrowDisabled: Bool

    let stepID: Int?

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Image(titleIcon)
                .renderingMode(.original)
                .resizable()
                .frame(widthHeight: appearance.titleIconSize)

            Text(titleText)
                .font(.title3)
                .foregroundColor(.primaryText)

            Spacer()

            NavigationLink(
                destination: {
                    if let stepID = stepID {
                        StepAssembly(stepID: stepID).makeModule()
                    } else {
                        EmptyView()
                    }
                },
                label: {
                    Image(arrowIcon)
                        .renderingMode(.original)
                        .resizable()
                        .frame(widthHeight: appearance.arrowIconSize)
                }
            )
            .disabled(stepID == nil)
        }
    }
}

struct ProblemTitle_Previews: PreviewProvider {
    static var previews: some View {
        ProblemOfDayTitle(
            appearance: .init(titleIconSize: 24),
            titleIcon: Images.Home.ProblemOfDay.calendar,
            titleText: Strings.ProblemOfDay.titleUncompleted,
            arrowIcon: Images.Home.ProblemOfDay.arrowUncompleted,
            isArrowDisabled: false,
            stepID: 4350
        )
        .previewLayout(.sizeThatFits)
    }
}
