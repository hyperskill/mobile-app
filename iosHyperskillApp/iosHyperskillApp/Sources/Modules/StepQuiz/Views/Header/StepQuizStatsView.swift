import SwiftUI

extension StepQuizStatsView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let iconWidthHeight: CGFloat = 12

        let primaryColor = Color.secondaryText
    }
}

struct StepQuizStatsView: View {
    private(set) var appearance = Appearance()

    var text: String

    var body: some View {
        HStack(spacing: appearance.interItemSpacing) {
            Image(Images.Step.clock)
                .renderingMode(.template)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.iconWidthHeight)

            Text(text)
                .font(.subheadline)
        }
        .foregroundColor(appearance.primaryColor)
    }
}

struct StepQuizStatsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizStatsView(text: Strings.choiceQuizStatText(users: 2438, hours: 13))

            StepQuizStatsView(text: Strings.choiceQuizStatText(users: 2438, hours: 13))
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
