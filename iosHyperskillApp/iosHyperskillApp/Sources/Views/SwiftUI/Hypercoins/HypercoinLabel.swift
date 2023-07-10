import SwiftUI

extension HypercoinLabel {
    struct Appearance {
        var titleFont = Font.body
        var titleForegroundColor = Color.primaryText

        var iconSize = CGSize(width: 32, height: 32)
    }
}

struct HypercoinLabel: View {
    private let title: Text

    private let appearance: Appearance

    init(@ViewBuilder title: () -> Text, appearance: Appearance = .init()) {
        self.title = title()
        self.appearance = appearance
    }

    init(title: String, appearance: Appearance = .init()) {
        self.title = Self.makeDefaultTitle(text: title, appearance: appearance)
        self.appearance = appearance
    }

    var body: some View {
        Label(
            title: {
                title
            },
            icon: {
                Image(Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(size: appearance.iconSize)
            }
        )
    }

    private static func makeDefaultTitle(text: String, appearance: Appearance) -> Text {
        Text(text)
            .font(appearance.titleFont)
            .foregroundColor(appearance.titleForegroundColor)
    }
}

struct HypercoinLabel_Previews: PreviewProvider {
    static var previews: some View {
        HypercoinLabel(title: "+15")
    }
}
